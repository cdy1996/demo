package com.cdy.demo.java.nio.file;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

/**
 * Created by 陈东一
 * 2018/1/18 15:03
 */
public class FileOutAndIN {
    
    static final String chartset = "utf-8";
    
    public static void nioTest() throws IOException {
        RandomAccessFile file = new RandomAccessFile("hello.txt", "rw");
        
     /*   File file1 = new File("D:\\workspace\\ideaworkspace\\springstudy\\hello.txt");
        BufferedWriter writer = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (file1,true),"UTF-8"));
        writer.write("撒旦撒旦");
        writer.close();*/
        FileChannel channel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(48);
        System.out.println("准备读出");
        int bytesRead = channel.read(buffer);
        byte[] bytes1 = new byte[1024];
        while (bytesRead != -1) {
            buffer.flip();

//            nioPrint1(buffer,bytes1);
            nioPrint2(buffer, bytes1);
            buffer.clear();
            bytesRead = channel.read(buffer);
        }
        
        System.out.println(new String(bytes1));
        System.out.println();
        System.out.println("准备写入");
        buffer.clear();
        byte[] bytes = "世界".getBytes(chartset);
        buffer.put(bytes);
        buffer.flip();
        while (buffer.hasRemaining()) {
            channel.write(buffer);
        }
        
        buffer.clear();
        channel.close();
        file.close();
    }
    
    private static void nioPrint2(ByteBuffer buffer, byte[] bytes1) throws CharacterCodingException {
        Charset charset = Charset.forName("UTF-8");
        System.out.println(charset.newDecoder().decode(buffer).toString());
    }
    
    
    public static void nioPrint1(ByteBuffer buffer, byte[] bytes1) {
        int i = 0;
        while (buffer.hasRemaining()) {
            bytes1[i] = buffer.get();
            i++;
//                System.out.print(buffer.get());
        }
    }
    
    public static void readTest() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\workspace\\ideaworkspace\\springstudy\\hello.txt"), chartset));
//        BufferedReader bufferedReader = new BufferedReader(new FileReader("D:\\workspace\\ideaworkspace\\springstudy\\hello.txt"));
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        bufferedReader.close();
        
    }
    
    public static void inputTest() throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("D:\\workspace\\ideaworkspace\\springstudy\\hello.txt"));
        byte[] bytes = new byte[1024];
        int length = 0;
        while ((length = bufferedInputStream.read(bytes)) != -1) {
            System.out.println(new String(bytes, chartset));
        }
        bufferedInputStream.close();
        
    }
    
    public static void outputTest() throws IOException {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("D:\\workspace\\ideaworkspace\\springstudy\\hello.txt"));
        bufferedOutputStream.write("1\n死死死".getBytes(chartset));
        bufferedOutputStream.close();
        
    }
    
    public static void writeTest() throws IOException {
        PrintWriter bufferedWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\workspace\\ideaworkspace\\springstudy\\hello.txt"), chartset)));
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("D:\\workspace\\ideaworkspace\\springstudy\\hello.txt"));
        bufferedWriter.println();
        bufferedWriter.write("1\n死死死");
        
        bufferedWriter.close();
        
    }
    
    public static FileInputStream testSystemIn() throws IOException {
        FileInputStream bufferedInputStream = new FileInputStream("D:\\workspace\\ideaworkspace\\springstudy\\hello.txt");
        return bufferedInputStream;
    }
    
    public static PrintStream testSystemOut() throws IOException {
        FileOutputStream bufferedOutputStream = new FileOutputStream("D:\\workspace\\ideaworkspace\\springstudy\\hello.txt");
        PrintStream printStream = new PrintStream(bufferedOutputStream);
        return printStream;
    }
    
    public static void main(String[] args) throws IOException {
//        writeTest();
//        readTest();
//        outputTest();
//        inputTest();
        nioTest();
//        System.setOut(testSystemOut());
//        System.out.println("123qweqwe");
//        System.out.close();

//        System.setIn(testSystemIn());
//        Scanner scanner = new Scanner(System.in);
//        String next = scanner.next();
//        System.out.println(next);
//        System.in.close();
    }
}
