package com.cdy.demo.middleware.rocketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * rocketmq 生产者
 * Created by 陈东一
 * 2020/7/27 0027 17:39
 */
public class RocketmqProducerDemo {
    
    
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("s2");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        Message msg = new Message();
        msg.setTopic("test-topic");
        msg.setTags("tag1");
        msg.setBody("11111".getBytes());
        SendResult send = producer.send(msg);
        System.out.printf("status=%s, msgId=%s, queue=%s\n", send.getSendStatus(), send.getMsgId(), send.getMessageQueue());
        
        
        msg = new Message();
        msg.setTopic("test-topic");
        msg.setTags("tag1");
        msg.setBody("mybody".getBytes());
        producer.send(msg);
        send = producer.send(msg);
        System.out.printf("status=%s, msgId=%s, queue=%s\n", send.getSendStatus(), send.getMsgId(), send.getMessageQueue());


//        msg = new Message();
//        msg.setTopic("test-topic");
//        msg.setTags("tag2");
//        msg.setBody("mybody".getBytes());
//        producer.send(msg);
//        send = producer.send(msg);
//        System.out.printf("status=%s, msgId=%s, queue=%s\n",send.getSendStatus(),send.getMsgId(),send.getMessageQueue());
//
        System.in.read();
    }
}
