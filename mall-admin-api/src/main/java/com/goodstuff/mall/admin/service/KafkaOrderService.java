package com.goodstuff.mall.admin.service;

import com.alibaba.fastjson.JSON;
import com.goodstuff.mall.db.domain.LitemallOrder;
import com.goodstuff.mall.db.service.LitemallOrderService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Properties;

/**
 * @ClassName KafkaOrderService
 * @Decription TODO
 * @Auther mac
 * @Date 2020-08-30 18:26
 * @Version 1.0
 **/
@Component
public class KafkaOrderService {

    protected static final Logger LOG = LoggerFactory.getLogger(KafkaOrderService.class);

    @Autowired
    private LitemallOrderService orderService;

    private static final String TOPIC = "education-info";
    private static final String BROKER_LIST = "39.100.126.178:9092";
    private static KafkaConsumer<String, String> kafkaConsumer = null;

    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG, "test");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return properties;
    }

    public void addOrder() {
        try {
            Properties properties = initConfig();
            kafkaConsumer = new KafkaConsumer<String, String>(properties);
            kafkaConsumer.subscribe(Arrays.asList(TOPIC));
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord record : records) {
                    try {
                        LOG.info("日志在这里:" + record);
                        LitemallOrder order = JSON.parseObject(record.value().toString(), LitemallOrder.class);
                        orderService.add(order);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kafkaConsumer.close();
        }
    }

}
