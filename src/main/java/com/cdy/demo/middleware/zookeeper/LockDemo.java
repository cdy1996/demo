package com.cdy.demo.middleware.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;

public class LockDemo {


    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .build();
        client.start();

        client.create().creatingParentContainersIfNeeded() // 递归创建所需父节点
                .withMode(CreateMode.PERSISTENT) // 创建类型为持久节点
                .forPath("/nodeA", "init".getBytes()); // 目录及内容

        client.delete()
                .guaranteed()  // 强制保证删除
                .deletingChildrenIfNeeded() // 递归删除子节点
                .withVersion(10086) // 指定删除的版本号
                .forPath("/nodeA");

        byte[] bytes = client.getData().forPath("/nodeA");
        System.out.println(new String(bytes));

        InterProcessMutex interProcessMultiLock = new InterProcessMutex(client, "/lock/a");

        interProcessMultiLock.acquire();

        interProcessMultiLock.release();


    }
}
