package com.goodstuff.mall.wx.web;

import com.goodstuff.mall.wx.annotation.LoginUser;
import com.goodstuff.mall.wx.service.KafkaOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName WxKafkaOrderController
 * @Decription TODO
 * @Auther mac
 * @Date 2020-08-30 17:55
 * @Version 1.0
 **/
@RestController
@RequestMapping("/wx/kafkaOrder")
@Validated
public class WxKafkaOrderController {

    @Autowired
    private KafkaOrderService kafkaOrderService;

    @GetMapping("list")
    public Object list() {
        return kafkaOrderService.addOrder();
    }
}
