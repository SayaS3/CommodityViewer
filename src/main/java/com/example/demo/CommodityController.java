package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commodities")
public class CommodityController {
    @Autowired
    private CommodityDataService commodityDataService;
    @Autowired
    private AlphaVantageService alphaVantageService;

    @GetMapping("/{type}")
    public CommodityData getCommodityData(@PathVariable CommodityType type) {
        if (commodityDataService.existsByType(type)) {
            return commodityDataService.findByType(type).orElse(null); // Zwróć istniejące dane lub null
        }

        Commodity commodity = alphaVantageService.fetchDataForCommodity(type);
        CommodityData commodityData = mapToCommodityData(commodity);
        return commodityDataService.save(commodityData);
    }


    private CommodityData mapToCommodityData(Commodity commodity) {
        CommodityData commodityData = new CommodityData();
        commodityData.setName(commodity.getName());
        commodityData.setInterval_column(commodity.getInterval_column());
        commodityData.setUnit(commodity.getUnit());
        commodityData.setCommodityType(CommodityType.valueOf(commodity.getCOMMODITY_TYPE()));
        commodityData.setData(commodity.getData());

        return commodityData;
    }

}