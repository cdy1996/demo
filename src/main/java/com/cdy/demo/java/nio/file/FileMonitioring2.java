package com.cdy.demo.java.nio.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * 文件路径监听 1.7实现
 * Created by 陈东一
 * 2018/5/25 20:21
 */
public class FileMonitioring2 {
    //用户目录和用户工作目录
    
    public static void main(String[] args) throws InterruptedException, IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        File userDir = new File(System.getProperty("user.dir"));
        Path path = userDir.toPath();
        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        while (true) {
            WatchKey poll = watchService.poll();
            if (poll != null) {
                List<WatchEvent<?>> watchEvents = poll.pollEvents();
                watchEvents.forEach(e -> {
                    if (e.kind().name().equals(StandardWatchEventKinds.ENTRY_CREATE.name())) {
                        Path context = (Path) e.context();
                        System.out.println("新建文件" + context);
                    }
                });
            }
        }
    }
    
}
