package com.cdy.demo.middleware.kafka;

import kafka.utils.ShutdownableThread;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static com.cdy.demo.middleware.kafka.KafkaProducerDemo.TOPIC;

/**
 * 手动提交
 *
 //high level
 //low level

 * Created by 陈东一
 * 2018/12/1 0001 18:14
 */
public class KafkaCustomerDemo2 extends ShutdownableThread {
    //broker list
    private static final String address = "127.0.0.1:9092";

    private final KafkaConsumer<Integer, String> comsumer;


    private List<ConsumerRecord> list = new ArrayList<>();

    public KafkaCustomerDemo2() {
        super("kafkaConsumerTest", false);
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, address);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
        //是否自动提交 offset
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        //自动提交 间隔时间
//        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        //设置使用最开始的offset偏移量为当前group, id的最早消息
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        //心跳时间
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.IntegerDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        this.comsumer = new KafkaConsumer<>(properties);

//        TopicPartition p = new TopicPartition(TOPIC, 0);
//        comsumer.assign(Collections.singleton(p));  //指定分区就不用订阅主题了 subscribe
    }

    @Override
    public void doWork() {
        comsumer.subscribe(Collections.singleton(TOPIC));
        ConsumerRecords<Integer, String> records = comsumer.poll(1000);
        records.forEach(e->{
            System.out.println("receiver message :["+e.key()+"->"+e.value()+"], offset: "+e.offset()+"");
            list.add(e);
        });

        if (list.size() >= 5) {
            System.out.println("commit");
            comsumer.commitSync();
            list.clear();
        }



    }

    public static void main(String[] args) {
        KafkaCustomerDemo2 kafkaCustomerDemo = new KafkaCustomerDemo2();
        kafkaCustomerDemo.start();


    }



}
