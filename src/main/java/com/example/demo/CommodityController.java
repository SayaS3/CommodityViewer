package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commodities")
public class CommodityController {

    @Autowired
    private AlphaVantageService alphaVantageService;

    @GetMapping("/{type}")
    public Commodity getCommodityData(@PathVariable CommodityType type) {
        return alphaVantageService.fetchDataForCommodity(type);
    }

}