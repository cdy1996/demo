//package com.cdy.demo.middleware.kafka;
//
//import org.apache.kafka.clients.producer.Callback;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//
//import java.io.IOException;
//import java.util.Properties;
//
///**
// * todo
// * Created by 陈东一
// * 2018/12/1 0001 18:06
// */
//public class KafkaProducerDemo {
//
//
//    private static final String address = "127.0.0.1:9092";
//
//    public static final String TOPIC = "test12";
//
//
//    private final KafkaProducer<Integer, String> producer;
//
//    private final String topic;
//
//    public KafkaProducerDemo(String topic) {
//        Properties properties = new Properties();
//        properties.put("bootstrap.servers", address);
//        properties.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
//        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        properties.put("client.id", "producerDemo");
//
//        this.producer = new KafkaProducer<>(properties);
//        this.topic = topic;
//
//    }
//
//
//    public void sendMsg() {
//        producer.send(new ProducerRecord<>(TOPIC, 1,"message"), new Callback() {
//            @Override
//            public void onCompletion(RecordMetadata metadata, Exception exception) {
//                System.out.println("message send to:["+metadata.partition()+"], offset:["+metadata.offset()+"]");
//            }
//        });
//    }
//
//
//    public static void main(String[] args) throws IOException {
//        KafkaProducerDemo kafkaProducer = new KafkaProducerDemo("test11");
//
//        kafkaProducer.sendMsg();
//        System.in.read();
//    }
//
//}
