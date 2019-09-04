package com.ncut.backmanagement.controller;

import com.ncut.backmanagement.common.ApiResponse;
import com.ncut.backmanagement.common.RentValueBlock;
import com.ncut.backmanagement.common.ServiceMultiResult;
import com.ncut.backmanagement.common.ServiceResult;
import com.ncut.backmanagement.common.VO.HouseDTO;
import com.ncut.backmanagement.common.VO.SupportAddressDTO;
import com.ncut.backmanagement.domain.RentSearch;
import com.ncut.backmanagement.service.IAddressService;
import com.ncut.backmanagement.service.SearchDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>FrontVisitController<br/>
 * <b>Description：</b>用户访问页面相关控制<br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/27 17:12<br/>
 */
@Controller
@RequestMapping(value = "/goodsFront")
public class FrontVisitController {
    @Autowired
    private IAddressService addressService;
    @Autowired
    private SearchDataService houseService;

    @RequestMapping(value = "/login")
    public String userLogin(){
        return "user/login";
    }

    @RequestMapping(value = "/center")
    public String center(){
        return "index";
    }

    @GetMapping(value = "/rentHouse")
    public String rentHouse(@ModelAttribute RentSearch rentSearch,
                            Model model, RedirectAttributes redirectAttributes){

        ServiceResult<SupportAddressDTO> city = addressService.findCity(rentSearch.getCityEnName());
        if (!city.isSuccess()) {
            redirectAttributes.addAttribute("msg", "必须选择城市！");
            return "redirect:/index";
        }
        model.addAttribute("currentCity", city.getResult());

        ServiceMultiResult<SupportAddressDTO> addressResult = addressService.findAllRegionsByCityName(rentSearch.getCityEnName());
        if (addressResult.getResult() == null || addressResult.getTotal() < 1) {
            redirectAttributes.addAttribute("msg", "must_chose_city");
            return "redirect:/index";
        }

        ServiceMultiResult<HouseDTO> serviceMultiResult = houseService.searchData(rentSearch);

        model.addAttribute("total", serviceMultiResult.getTotal());
        model.addAttribute("houses", serviceMultiResult.getResult());

        if (rentSearch.getRegionEnName() == null) {
            rentSearch.setRegionEnName("*");
        }
        model.addAttribute("searchBody", rentSearch);
        model.addAttribute("regions", addressResult.getResult());

        model.addAttribute("priceBlocks", RentValueBlock.PRICE_BLOCK);
        model.addAttribute("areaBlocks", RentValueBlock.AREA_BLOCK);

        model.addAttribute("currentPriceBlock", RentValueBlock.matchPrice(rentSearch.getPriceBlock()));
        model.addAttribute("currentAreaBlock", RentValueBlock.matchArea(rentSearch.getAreaBlock()));
        if ("bj".equals(rentSearch.getCityEnName())) {
            return "rent-list";
        }
        return null;
    }

    @GetMapping(value = "/autocomplete")
    // 注明返回数据
    @ResponseBody
    public ApiResponse autocomplete(@RequestParam("prefix") String prefix){
        if (prefix.isEmpty()) {
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }
/*        List<String> result = new ArrayList<>();
        result.add("中国共产党好");
        result.add("中国共产党万岁");*/
        ServiceResult<List<String>> result = houseService.suggest(prefix);
        return ApiResponse.ofSuccess(result.getResult());
    }
}
