package com.cdy.demo.java.classloadertest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FooClassLoader extends ClassLoader {


    public static final String NAME = "/demo/demo/classloaderpath/";

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass == null) {
            String s = name.substring(name.lastIndexOf(".") + 1) + ".class";
            System.out.println(NAME + s);
            File file = new File(NAME + s);
            try (FileInputStream fileInputStream = new FileInputStream(file)){
                byte[] bytes = new byte[fileInputStream.available()];
                fileInputStream.read(bytes);
                return defineClass(name, bytes, 0, bytes.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loadedClass;
    }
}
