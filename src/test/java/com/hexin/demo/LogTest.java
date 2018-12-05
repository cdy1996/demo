package com.hexin.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

    public static Logger log = LoggerFactory.getLogger(LogTest.class);

    public static void main(String[] args) {
        int i = 3;
        System.out.println(i++ + i++ + ++i);
    }
}
