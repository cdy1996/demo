package com.cdy.demo.middleware.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * rocketmq 消费者
 * Created by 陈东一
 * 2020/7/27 0027 17:39
 */
public class RocketmqConsumerDemo {
    
    
    public static void main(String[] args) throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("s2");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.subscribe("test-topic", "*"); // 同一个 消费组如果 tag不同会消费混乱, 要实现不同业务不同tag需要使用不同消费组
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (int i = 0; i < msgs.size(); i++) {
                    MessageExt msg = msgs.get(i);
                    System.out.printf("topic=%s, tags=%s, msg=%s, key=%s", msg.getTopic(), msg.getTags(), new String(msg.getBody()), msg.getKeys());
                    if (msg.getTags().contains("order_create")) {
                        //商家减库存，调用Stock API；
                        System.out.println("库存扣减:" + msg.getTransactionId());
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        
        System.in.read();
    }
    
}
