package com.cdy.demo.repeatedWheels.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接池 甲东南半
 * Created by 陈东一
 * 2018/3/4 20:39
 */
public class MyDataSource {
    
    //当前活跃数量
    private AtomicInteger currentActive = new AtomicInteger();
    
    //空连接池
    private Vector<Connection> freePools = new Vector<>();
    
    //工作连接池
    private Vector<MyPoolEntry> activePools = new Vector<>();
    
    private MyPoolConfig config;
    
    public static class Singleton {
        private static final MyDataSource DATA_SOURCE = new MyDataSource();
        
        private Singleton() {
        }
        
        public static final MyDataSource getInstance() {
            return DATA_SOURCE;
        }
    }
    
    private MyDataSource() {
        config = MyPoolConfig.Singleton.getInstance();
        try {
            init();
            check();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //创建连接池
    private void init() throws Exception {
        Class.forName(config.getDirver());
        for (int i = 0; i < config.getInitSize(); i++) {
            Connection connection = createConnection();
            freePools.add(connection);

        }
    }

    //创建链接
    private Connection createConnection() throws Exception {
        Connection connection = DriverManager.getConnection(config.getUrl(), config.getUser(), config.getPassword());
        currentActive.incrementAndGet();
        System.out.println("创建一个新的连接");
        return connection;
    }
    
    //获取连接
    public synchronized Connection getConnection() throws Exception {
        Connection connection = null;
        if (freePools.size() > 0) {
            connection = freePools.get(0);
            freePools.remove(0);
        } else {
            if (currentActive.get() < config.getMaxSize()) {
                connection = createConnection();
            } else {
                System.out.println("线程池已满,等待一秒");
                wait(1000);
                return getConnection();
            }
        }
        long current = System.currentTimeMillis();
        MyPoolEntry entry = new MyPoolEntry(connection, current);
        activePools.add(entry);
        return connection;
    }
    
    //回收链接
    public void releaseConnection(Connection connection) throws SQLException {
        if (!connection.isClosed()) {
            freePools.add(connection);
            System.out.println("回收一个链接,当前空余连接数" + currentActive.get());

        }
    }
    
    
    //销毁链接
    public void delete(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    //监控数据库连接
    private void check() {
        //判断是否超时事件
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("大王叫我来巡山,活跃线程数" + activePools.size());
                
                activePools.forEach(e -> {
                    long startTIme = e.getUseStartTIme();
                    long current = System.currentTimeMillis();
                    long l = current - startTIme;
                    if (l >= config.getTimeout()) {
                        System.out.println("超时" + (l - config.getTimeout()) + "需要强制回收");
                        try {
                            releaseConnection(e.getConnection());
                            activePools.remove(e);
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

                if (freePools.size() > config.getPoolSize()) {
                    freePools.stream().limit(freePools.size() - config.getPoolSize()).forEach(e -> {
                        try {
                            e.close();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                    });
                }
                
            }
        }, 3000, 3000);

    }
    
}
