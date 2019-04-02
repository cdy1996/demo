package com.cdy.demo.java;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;

public class ResourceBundleTest {

    @Test
    public void test(){
        ClassPathResource classPathResource = new ClassPathResource("abc.properties");
//        DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
//        Resource resource = defaultResourceLoader.getResource("classpath:abc.properties");


        try (InputStream is = classPathResource.getInputStream()){
            PropertyResourceBundle propertyResourceBundle = new PropertyResourceBundle(is);
            Enumeration<String> keys = propertyResourceBundle.getKeys();

            while (keys.hasMoreElements()) {
                String string = propertyResourceBundle.getString(keys.nextElement());
                System.out.println(string);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
