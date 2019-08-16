package com.ncut.backmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// 使用@Controller注解标记HouseDataHandleController类，则该类下的方法可以返回jsp、html等页面，也可以通过对某些方法标记@ResponseBody实现返回json等数据
@Controller
public class GoodsManagementController {

    @RequestMapping(value = "/admin/login")
    public String adminLogin(){
        return "huiAdmin/login";
    }

    @RequestMapping(value = "/admin/index")
    public String adminCenter(){
        return "huiAdmin/index";
    }

    @RequestMapping(value = "/admin/welcome")
    public String adminWelcome(){
        return "huiAdmin/welcome";
    }

    @RequestMapping(value = "/admin/pictureList")
    public String adminPictureList(){
        return "huiAdmin/picture-list";
    }

    @RequestMapping(value = "/admin/articleList")
    public String adminArticleList(){
        return "huiAdmin/article-list";
    }

    @RequestMapping(value = "/admin/productBrand")
    public String adminProductBrand(){
        return "huiAdmin/product-brand";
    }

    @RequestMapping(value = "/admin/productCategory")
    public String adminProductCategory(){
        return "huiAdmin/product-category";
    }

    @RequestMapping(value = "/admin/productAdminAdd")
    public String adminAdminAdd(){
        return "huiAdmin/admin-add";
    }

    @RequestMapping(value = "/admin/producCategoryAdd")
    public String adminProducCategoryAdd(){
        return "huiAdmin/product-category-add";
    }

    @RequestMapping(value = "/admin/productList")
    public String adminProductList(){
        return "huiAdmin/product-list";
    }

    @RequestMapping(value = "/admin/productShow")
    public String adminProductShow(){
        return "huiAdmin/product-show";
    }
}
