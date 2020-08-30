package com.goodstuff.mall.admin.web;

import com.goodstuff.mall.admin.service.KafkaOrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.goodstuff.mall.core.util.ResponseUtil;
import com.goodstuff.mall.db.service.LitemallGoodsProductService;
import com.goodstuff.mall.db.service.LitemallGoodsService;
import com.goodstuff.mall.db.service.LitemallOrderService;
import com.goodstuff.mall.db.service.LitemallUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
@Validated
public class AdminDashbordController {
    private final Log logger = LogFactory.getLog(AdminDashbordController.class);

    @Autowired
    private LitemallUserService userService;
    @Autowired
    private LitemallGoodsService goodsService;
    @Autowired
    private LitemallGoodsProductService productService;
    @Autowired
    private LitemallOrderService orderService;
    @Autowired
    private KafkaOrderService kafkaOrderService;

    @GetMapping("")
    public Object info() {
        int userTotal = userService.count();
        int goodsTotal = goodsService.count();
        int productTotal = productService.count();
        int orderTotal = orderService.count();
        Map<String, Integer> data = new HashMap<>();
        data.put("userTotal", userTotal);
        data.put("goodsTotal", goodsTotal);
        data.put("productTotal", productTotal);
        data.put("orderTotal", orderTotal);

        return ResponseUtil.ok(data);
    }

}
