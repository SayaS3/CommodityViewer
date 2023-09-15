package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DataInitializationService {

    @Autowired
    private CommodityDataService commodityDataService;

    @Autowired
    private AlphaVantageService alphaVantageService;

    @PostConstruct
    public void initializeData() {
        fetchDataAndSave();
    }

    @Scheduled(fixedRate = 3600000) // co godzinę
    public void fetchDataAndSave() {
        for (CommodityType type : CommodityType.values()) {
            if (!commodityDataService.existsByType(type)) {
                Commodity commodity = alphaVantageService.fetchDataForCommodity(type);
                CommodityData commodityData = mapToCommodityData(commodity);
                commodityDataService.save(commodityData);
            }
        }
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
