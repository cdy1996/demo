package com.cdy.demo.java.agent;

/**
 * 主方法
 * Created by 陈东一
 * 2019/5/19 0019 19:25
 */
public class AccountMain {
    public static void main(String[] args) throws InterruptedException {
        for (;;) {
            new Account().operation();
            Thread.sleep(5000);
        }
    }
}
