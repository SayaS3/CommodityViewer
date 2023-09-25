package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public void runPythonScript() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "resources/python/main.py");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        System.out.println("Uruchamian skrypt:");
        runPythonScript(); // Uruchom skrypt Pythona po pobraniu i zapisaniu danych
        System.out.println("Skrypt zostal wykonany");
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
