package com.ncut.goodsmanagement.controller;

import com.ncut.goodsmanagement.service.GoodsManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsManagementController {
    @Autowired
    GoodsManagementService goodsManagementService;
    @RequestMapping(value = "/addgoods")
    public String addGoods(){
        goodsManagementService.addGoods();
        return "ssssssssssssssssss";
    }

    @RequestMapping(value = "/admin/welcome")
    public String adminCenter(){
        return "huiAdmin/welcome";
    }
}
