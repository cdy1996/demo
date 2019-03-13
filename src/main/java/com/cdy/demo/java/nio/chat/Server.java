package com.cdy.demo.java.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

public class Server {

    Selector selector;
    Charset charset = Charset.forName("utf-8");
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    Map<String, SocketChannel> users = new HashMap<>();

    public Server(int port) {
        try {
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));

            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void listen() throws IOException {
        while (true) {
            int count = selector.select();
            if (count == 0) continue;

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                iterator.remove();
                if (next.isAcceptable()) {

                    ServerSocketChannel channel = (ServerSocketChannel) next.channel();
                    System.out.println(channel + " 可连接");
                    SocketChannel client = channel.accept();
                    client.configureBlocking(false);
                    System.out.println("接收客户端 " + client);
                    client.register(selector, SelectionKey.OP_READ);
                    client.write(charset.encode("#name#"));
//                    next.interestOps(SelectionKey.OP_ACCEPT);

                }
                String name = "";
                if (next.isReadable()) {
                    SocketChannel channel = null;
                    try {

                        channel = (SocketChannel) next.channel();
                        System.out.println(channel + " 可读");
                        int read = channel.read(buffer);
//                        String receive = new String(buffer.array(),"utf-8");
                        buffer.flip();
                        String receive = new String(charset.decode(buffer).array());
//                        System.out.println("服务端接收数据 " + receive);

                        buffer.clear();
                        if (receive.startsWith("#name#")) {
                            String user = receive.substring(5);
                            users.put(user, channel);
                            receive = "加入聊天室";
                            name = user;
                        }

                        for (Map.Entry<String, SocketChannel> entry : users.entrySet()) {
                            if (entry.getValue() == channel) {
                                name = entry.getKey();
                            }

                        }
                        for (Map.Entry<String, SocketChannel> entry : users.entrySet()) {
                            if (entry.getValue() != channel)
                                entry.getValue().write(charset.encode(name + ":" + receive));
                        }

//                        next.interestOps(SelectionKey.OP_WRITE);
                    } catch (IOException e) {
                        System.out.println("客户端异常 " + channel);
                        users.remove(name);
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
        new Server(8999).listen();
    }

}
