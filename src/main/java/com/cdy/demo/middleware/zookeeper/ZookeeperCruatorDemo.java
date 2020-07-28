package com.cdy.demo.middleware.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ZookeeperCruatorDemo {
    
    
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "zookeeper-state");
            }
        });
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString("127.0.0.1:2181")
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();
        client.start();
        System.out.println(client.getState());
        client.getConnectionStateListenable().addListener((c, s) -> {
            System.out.println("---------------------------------");
            System.out.println(s);
        }, executorService);
        try {
            client.create().creatingParentContainersIfNeeded() // 递归创建所需父节点
                    .withMode(CreateMode.PERSISTENT) // 创建类型为持久节点
                    .forPath("/nodeA", "init".getBytes()); // 目录及内容
        } catch (Exception e) {
            System.out.println("---------------------------------");
            e.printStackTrace(); //ConnectionLossException   SessionMovedException  SessionExpiredException
        }
        
        try {
            client.delete()
                    .guaranteed()  // 强制保证删除
                    .deletingChildrenIfNeeded() // 递归删除子节点
                    .withVersion(10086) // 指定删除的版本号
                    .forPath("/nodeA");
        } catch (Exception e) {
            System.out.println("---------------------------------");
            e.printStackTrace();  // BadVersionException
        }
        
        byte[] bytes = client.getData().forPath("/nodeA");
        System.out.println(new String(bytes));

//        InterProcessMutex interProcessMultiLock = new InterProcessMutex(client, "/lock/a");
//
//        interProcessMultiLock.acquire();
//
//        interProcessMultiLock.release();
    
    
    }
}
