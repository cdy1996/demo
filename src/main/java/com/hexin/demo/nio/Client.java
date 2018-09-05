package com.hexin.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Client {

    Selector selector;
    static Charset charset = Charset.forName("utf-8");
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    static SocketChannel channel;

    public Client(int port) {
        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);

            channel.connect(new InetSocketAddress("127.0.0.1", port));

            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_CONNECT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void process() throws IOException {
        while (true) {
            int count = selector.select();
            if (count == 0) continue;

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                iterator.remove();
                if (next.isConnectable()) {

                    SocketChannel channel = (SocketChannel) next.channel();
                    System.out.println(channel + " 可连接");
                    channel.finishConnect();
                    channel.register(selector, SelectionKey.OP_READ);

                }
                if (next.isReadable()) {
                    SocketChannel channel = null;
                    try {

                        channel = (SocketChannel) next.channel();
                        System.out.println(channel + " 可读");
                        int read = channel.read(buffer);
//                        String receive = new String(buffer.array(),"utf-8");
                        buffer.flip();
                        String receive = new String(charset.decode(buffer).array());

                        System.out.println(receive);
                        buffer.clear();
//                        channel.write(charset.encode(receive));
//                        next.interestOps(SelectionKey.OP_WRITE);
                    } catch (IOException e) {
                        System.out.println("客户端异常 " + channel);
                        channel.close();
                        next.cancel();
                    }

                }
//                if (next.isWritable()) {
//                    SocketChannel channel = (SocketChannel) next.channel();
//                    Scanner scanner = new Scanner(System.in);
//                    System.out.println(channel + " 可写");
//                    String s = scanner.nextLine();
//                    ByteBuffer wrap = ByteBuffer.wrap(s.getBytes(charset));
//                    channel.write(wrap);
////                    next.interestOps(SelectionKey.OP_READ);
//                }

            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Thread(() -> {
            try {
                new Client(8999).process();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入");
        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            ByteBuffer wrap = ByteBuffer.wrap(s.getBytes(charset));
            try {
                channel.write(wrap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("请输入");
        }
    }

}
