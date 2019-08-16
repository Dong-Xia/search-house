package com.ncut.goodsmanagement.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.ncut.goodsmanagement.service.GoodsManagementService;
import org.springframework.stereotype.Component;

@Service
@Component
public class GoodsManagementServiceImpl implements GoodsManagementService {
    public void addGoods() {
        System.out.println("新增商品成功！");
    }
}
