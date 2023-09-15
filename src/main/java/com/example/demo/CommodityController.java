package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commodity/")
public class CommodityController {

    @Autowired
    private CommodityDataService commodityDataService;

    @GetMapping("/{type}")
    public CommodityData getCommodityData(@PathVariable CommodityType type) {
        return commodityDataService.findByType(type).orElse(null);
    }
}
