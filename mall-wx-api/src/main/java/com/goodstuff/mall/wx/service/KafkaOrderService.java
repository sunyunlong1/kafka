package com.goodstuff.mall.wx.service;

import com.alibaba.fastjson.JSONObject;
import com.goodstuff.mall.db.domain.LitemallOrder;
import com.goodstuff.mall.db.service.LitemallOrderService;
import com.goodstuff.mall.db.util.OrderUtil;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Properties;

/**
 * @ClassName KafkaOrderService
 * @Decription TODO
 * @Auther mac
 * @Date 2020-08-30 17:45
 * @Version 1.0
 **/
@Component
public class KafkaOrderService {

    protected static final Logger LOG = LoggerFactory.getLogger(KafkaOrderService.class);

    @Autowired
    private LitemallOrderService orderService;

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

    public boolean addOrder() {
        try {
            LitemallOrder order = null;
            Integer userId = 1;
            order = new LitemallOrder();
            order.setUserId(userId);
            order.setOrderSn(orderService.generateOrderSn(userId));
            order.setOrderStatus(OrderUtil.STATUS_AUTO_CANCEL);
            order.setConsignee("测试用户");
            order.setMobile("18404552888");
            order.setMessage("尽快送达");
            order.setAddress("金隅智造工厂N3");
            order.setGoodsPrice(new BigDecimal("500"));
            order.setFreightPrice(new BigDecimal("10.00"));
            order.setCouponPrice(new BigDecimal("0"));
            order.setIntegralPrice(new BigDecimal("0"));
            order.setOrderPrice(new BigDecimal("510"));
            order.setActualPrice(new BigDecimal("510"));
            order.setGrouponPrice(new BigDecimal(0.00));

            ProducerRecord<String, String> record = new ProducerRecord<String, String>(TOPIC, JSONObject.toJSONString(order));
            RecordMetadata recordMetadata = producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (null == exception) {
                        LOG.info("发送成功");
                    }
                    if (null != metadata) {
                        LOG.info("offset:" + metadata.offset() + ";partition:" + metadata.partition());
                    }
                }
            }).get();
            return recordMetadata == null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            producer.close();
        }
    }
}
