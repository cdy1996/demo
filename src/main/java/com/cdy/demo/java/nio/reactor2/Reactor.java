package com.cdy.demo.java.nio.reactor2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * nio reactor的一种实现
 */
public class Reactor implements Runnable {

    Selector selector;
    ServerSocketChannel channel;
    Charset charset = Charset.forName("utf-8");
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    Map<String, SocketChannel> users = new HashMap<>();

    public Reactor(int port) {
        try {
            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));

            selector = Selector.open();
            SelectionKey register = channel.register(selector, SelectionKey.OP_ACCEPT);
            register.attach(new Accpetor());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()) {
                    //Reactor负责dispatch收到的事件
                    dispatch((SelectionKey) (it.next()));
                }
                selected.clear();
            }
        } catch (IOException ex) { /* ... */ }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) (k.attachment());
        //调用之前注册的callback对象
        if (r != null) {
            r.run();
        }
    }


    class Accpetor implements Runnable {

        @Override
        public void run() {
            try {
                SocketChannel accept = channel.accept();
                if (accept != null) {
                    new Handler(selector, accept);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class Handler implements Runnable {
        final SocketChannel channel;
        final SelectionKey sk;
        ByteBuffer input = ByteBuffer.allocate(1024);
        ByteBuffer output = ByteBuffer.allocate(1024);
        static final int READING = 0, SENDING = 1;
        int state = READING;

        public Handler(Selector selector, SocketChannel c) throws IOException {
            channel = c;
            c.configureBlocking(false);
            // Optionally try first read now
            sk = channel.register(selector, 0);
            //将Handler作为callback对象
            sk.attach(this);

            //第二步,注册Read就绪事件
            sk.interestOps(SelectionKey.OP_READ);
            selector.wakeup();
        }

        boolean inputIsComplete() {
            /* ... */
            return false;
        }

        boolean outputIsComplete() {

            /* ... */
            return false;
        }

        void process() {
            /* ... */
            return;
        }

        public void run() {
            try {
                if (state == READING) {
                    read();
                } else if (state == SENDING) {
                    send();
                }
            } catch (IOException ex) { /* ... */ }
        }

        void read() throws IOException {
            channel.read(input);
            if (inputIsComplete()) {

                process();

                state = SENDING;
                // Normally also do first write now

                //第三步,接收write就绪事件
                sk.interestOps(SelectionKey.OP_WRITE);
            }
        }

        void send() throws IOException {
            channel.write(output);

            //write完就结束了, 关闭select key
            if (outputIsComplete()) {
                sk.cancel();
            }
        }


    }

    class MHandler implements Runnable {
        final SocketChannel channel;
        final SelectionKey selectionKey;
        ByteBuffer input = ByteBuffer.allocate(1024);
        ByteBuffer output = ByteBuffer.allocate(1024);
        static final int READING = 0, SENDING = 1;
        int state = READING;

        ExecutorService pool = Executors.newFixedThreadPool(2);
        static final int PROCESSING = 3;

        public MHandler(Selector selector, SocketChannel c) throws IOException {
            channel = c;
            c.configureBlocking(false);
            // Optionally try first read now
            selectionKey = channel.register(selector, 0);
            //将Handler作为callback对象
            selectionKey.attach(this);

            //第二步,注册Read就绪事件
            selectionKey.interestOps(SelectionKey.OP_READ);
            selector.wakeup();
        }

        boolean inputIsComplete() {
            /* ... */
            return false;
        }

        boolean outputIsComplete() {

            /* ... */
            return false;
        }

        void process() {
            /* ... */
            return;
        }

        public void run() {
            try {
                if (state == READING) {
                    read();
                } else if (state == SENDING) {
                    send();
                }
            } catch (IOException ex) { /* ... */ }
        }

        synchronized void read() throws IOException {
            // ...
            channel.read(input);
            if (inputIsComplete()) {
                state = PROCESSING;
                //使用线程pool异步执行
                pool.execute(new Processer());
            }
        }

        void send() throws IOException {
            channel.write(output);

            //write完就结束了, 关闭select key
            if (outputIsComplete()) {
                selectionKey.cancel();
            }
        }

        synchronized void processAndHandOff() {
            process();
            state = SENDING;
            // or rebind attachment
            //process完,开始等待write就绪
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }

        class Processer implements Runnable {
            public void run() {
                processAndHandOff();
            }
        }
    }
}


