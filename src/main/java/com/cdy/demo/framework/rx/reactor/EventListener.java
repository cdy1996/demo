package com.cdy.demo.framework.rx.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

interface MyEventListener<T> {
    void onDataChunk(List<T> chunk);
    
    void processComplete();
}

/**
 * todo
 * Created by 陈东一
 * 2020/4/12 0012 15:47
 */
public class EventListener {
    
    MyEventProcessor myEventProcessor = new MyEventProcessor();
    
    @Test
    public void testCreate() {
        Flux<String> bridge = Flux.create(sink -> {
            myEventProcessor.register(
                    new MyEventListener<String>() {
                        // 推
                        public void onDataChunk(List<String> chunk) {
                            for (String s : chunk) {
                                sink.next(s);
                            }
                        }
                        
                        public void processComplete() {
                            sink.complete();
                        }
                    });
            
            sink.onRequest(n -> {  // 拉
                List<String> messages = myEventProcessor.request(n);
                for (String s : messages) {
                    sink.next(s);
                }
            });
        });
        
    }
    
    @Test
    public void testPush() {
        Flux<String> bridge = Flux.push(sink -> {
            myEventProcessor.register(
                    new SingleThreadEventListener<String>() {
                        
                        public void onDataChunk(List<String> chunk) {
                            for (String s : chunk) {
                                sink.next(s);
                            }
                        }
                        
                        public void processComplete() {
                            sink.complete();
                        }
                        
                        public void processError(Throwable e) {
                            sink.error(e);
                        }
                    });
        });
        
    }
    
    
}

class SingleThreadEventListener<T> implements MyEventListener<T> {
    
    @Override
    public void onDataChunk(List<T> chunk) {
    }
    
    @Override
    public void processComplete() {
    
    }
}


class MyEventProcessor {
    List<MyEventListener<String>> list = new ArrayList<>();
    List<String> queue = new ArrayList<>();
    
    public void register(MyEventListener<String> listener) {
        list.add(listener);
    }
    
    public List<String> request(long n) {
        if (n < queue.size()) {
            List<String> strings = queue.stream().limit(n).collect(Collectors.toList());
            queue.removeAll(strings);
            return strings;
        } else {
            List<String> strings = queue.stream().collect(Collectors.toList());
            queue.clear();
            return strings;
        }
    }
    
    public void dataChuck(String... s) {
        list.forEach(e -> {
            List<String> ts = Arrays.asList(s);
            e.onDataChunk(ts);
        });
    }
    
    public void processComplete() {
        list.forEach(MyEventListener::processComplete);
    }
    
}