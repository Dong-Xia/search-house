package com.ncut.goodsserviceconsumer.controller;

import com.ncut.goodsserviceconsumer.service.GoodsConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsConsumerController {
    @Autowired
    GoodsConsumer goodsConsumer;
    @RequestMapping(value = "/addgoods")
    public String addGoods(){
        goodsConsumer.addGoods();
        return "ssssssssssssssssss";
    }

}
