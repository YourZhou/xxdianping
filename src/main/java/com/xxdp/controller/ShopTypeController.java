package com.xxdp.controller;

import com.xxdp.dto.Result;
import com.xxdp.entity.ShopType;
import com.xxdp.service.ShopTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/shop-type")
public class ShopTypeController {

    @Resource
    private ShopTypeService shopTypeService;

    @GetMapping("list")
    public Result queryTypeList() {
        List<ShopType> shopTypes = shopTypeService.query().orderByAsc("sort").list();
        return Result.ok(shopTypes);
    }
}
