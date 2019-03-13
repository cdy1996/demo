package com.cdy.demo.repeatedWheels.myJdbcPool;

import java.sql.Connection;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by 陈东一
 * 2018/3/4 20:54
 */
public class Test {
    
    public static void main(String[] args) {
        for (int i = 0; i < 15; i++) {
            new Thread(() -> {

                try {
                    MyDataSource dataSource = MyDataSource.Singleton.getInstance();
                    Connection connection = dataSource.getConnection();
                    System.out.println(connection);
                    Random random = new Random();
                    int time = random.nextInt(10);
                    TimeUnit.SECONDS.sleep(time);
                    dataSource.releaseConnection(connection);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }).start();
        }
    }
}
