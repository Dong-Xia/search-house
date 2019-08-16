package com.ncut.backmanagement.controller;

import com.ncut.backmanagement.service.DataSynService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 使用@RestController注解标记HouseDataHandleController类，则该类下的所有方法都只能返回数据，不能返回jsp、html等页面
@RestController
@RequestMapping(value = "/house")
public class HouseDataHandleController {

    @Autowired
    private DataSynService dataSynService;
    /**
     * 数据同步操作
     * @return
     */
    @RequestMapping(value = "/dataSyn")
    public void dataSyn(){
        dataSynService.synData();
    }

    /**
     * 数据同步操作
     * @return
     */
    @RequestMapping(value = "/newDataSyn")
    public void newDataSyn(){
        dataSynService.addSingleNewDataToEs(26);
        System.out.println("成功");
    }

    /**
     * 数据同步操作
     * @return
     */
    @RequestMapping(value = "/deleteDataSyn")
    public void deleteDataSyn(){
        dataSynService.deleteSingleDataFromEs(26);
        System.out.println("成功");
    }

    /**
     * 数据同步操作
     * @return
     */
    @RequestMapping(value = "/updateDataSyn")
    public void updateDataSyn(){
        dataSynService.updateSingleDataToEs(26);
        System.out.println("成功");
    }
}
