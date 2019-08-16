package com.ncut.goodsserviceconsumer.service.impl;

import com.ncut.goodsmanagement.service.GoodsManagementService;
import com.ncut.goodsserviceconsumer.service.GoodsConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsConsumerImpl implements GoodsConsumer {
    @Autowired
    GoodsManagementService goodsManagementService;

    public void addGoods() {
        goodsManagementService.addGoods();
    }
}
