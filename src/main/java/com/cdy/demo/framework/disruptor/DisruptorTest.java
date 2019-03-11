package com.cdy.demo.framework.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorTest {


    public static void main(String[] args) throws IOException {

        WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();
        WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();
        WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();


        EventFactory<LongEvent> eventFactory = new LongEventFactory();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        int ringBufferSize = 2; // RingBuffer 大小，必须是 2 的 N 次方；


        Disruptor<LongEvent> disruptor = new Disruptor<>(eventFactory,
                ringBufferSize, Executors.defaultThreadFactory(), ProducerType.MULTI,
                new BlockingWaitStrategy());

        WorkHandler<LongEvent> eventHandler = (event) -> {
            Disruptor<LongEvent> disruptor1 = disruptor;
            System.out.println("消费者1消费消息:" + event);
        };

        WorkHandler<LongEvent> eventHandler2 = (event) ->
                System.out.println(2 + ":");

        WorkHandler<LongEvent> eventHandler3 = (event) ->
                System.out.println(3 + ":");

//        EventHandler<LongEvent> eventHandler1 = new LongEventHandler();
//        EventHandler<LongEvent> eventHandler2 = new LongEventHandler();
        EventHandlerGroup<LongEvent> group =
                disruptor.handleEventsWithWorkerPool(eventHandler);
//        group.thenHandleEventsWithWorkerPool(eventHandler3);
//        group.and()
//        disruptor.handleEventsWithWorkerPool(eventHandler);
//        disruptor.handleEventsWithWorkerPool(eventHandler,eventHandler1,eventHandler2);
        disruptor.start();


        // 发布事件；
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
//        ByteBuffer allocate = ByteBuffer.allocate(1024);
//        allocate.put("111111".getBytes());
//        new LongEventProducer(ringBuffer).onData(allocate);


        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                long sequence = ringBuffer.next();//请求下一个事件序号；
                System.out.println("生产者的下一个序号" + sequence);
                System.out.println("ringuffer游标" + ringBuffer.getCursor());
                System.out.println("ringuffer可用空间" + ringBuffer.remainingCapacity());
                try {
                    LongEvent event = ringBuffer.get(sequence);//获取该序号对应的事件对象；
                    //获取要通过事件传递的业务数据；
                    event.set(finalI);
                } finally {
                    ringBuffer.publish(sequence);//发布事件；
                }
            }).start();
        }



      /*  long sequence1 = ringBuffer.next();//请求下一个事件序号；
        try {
            LongEvent observer = ringBuffer.get(sequence1);//获取该序号对应的事件对象；
            long data = 2L;//获取要通过事件传递的业务数据；
            observer.set(data);
        } finally {
            ringBuffer.publish(sequence1);//发布事件；
        }

        long sequence2 = ringBuffer.next();//请求下一个事件序号；
        try {
            LongEvent observer = ringBuffer.get(sequence2);//获取该序号对应的事件对象；
            long data = 3L;//获取要通过事件传递的业务数据；
            observer.set(data);
        } finally {
            ringBuffer.publish(sequence2);//发布事件；
        }*/
        System.out.println("!11111");
        System.in.read();
        disruptor.shutdown();//关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；
    }
}
