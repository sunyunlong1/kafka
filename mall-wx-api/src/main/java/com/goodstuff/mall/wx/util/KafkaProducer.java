package com.goodstuff.mall.wx.util;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @ClassName KafkaProducer
 * @Decription TODO
 * @Auther mac
 * @Date 2020-08-30 15:40
 * @Version 1.0
 **/
public class KafkaProducer {
    private static final String TOPIC = "education-info";
    private static final String BROKER_LIST = "39.100.126.178:9092";
    private static org.apache.kafka.clients.producer.KafkaProducer<String, String> producer = null;

    static {
        Properties configs = initConfig();
        producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(configs);
    }

    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    public static void main(String[] args) {
        try {
            String message = "你好-----------------------------------------";
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC, message);
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (null == exception) {
                        System.out.println("perfect!");
                    }
                    if (null != metadata) {
                        System.out.print("offset:" + metadata.offset() + ";partition:" + metadata.partition());
                    }
                }
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
