package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

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
            File scriptFile = new File("target/classes/python/main.py");
            if (scriptFile.exists()) {
                ProcessBuilder processBuilder = new ProcessBuilder("python", scriptFile.getAbsolutePath());
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
            } else {
                System.out.println("Plik skryptu nie istnieje w lokalizacji: " + scriptFile.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void fetchDataAndSave() {
        System.out.println("Rozpoczynam pobieranie i zapisywanie danych");
        for (CommodityType type : CommodityType.values()) {
            System.out.println("Rozpoczynam iterację dla " + type);
            if (!commodityDataService.existsByType(type)) {
                Commodity commodity = alphaVantageService.fetchDataForCommodity(type);
                System.out.println("Przed mapToCommodityData");
                CommodityData commodityData = mapToCommodityData(commodity);
                System.out.println("Po mapToCommodityData");
                commodityDataService.save(commodityData);
                System.out.println("Zakończono iterację dla " + type);
            }
        }
        System.out.println("Uruchamian skrypt:");
        runPythonScript(); // Uruchom skrypt Pythona po pobraniu i zapisaniu danych
        System.out.println("Skrypt został wykonany");
    }



    private CommodityData mapToCommodityData(Commodity commodity) {
        System.out.println("Rozpoczynam mapowanie danych");
        CommodityData commodityData = new CommodityData();
        commodityData.setName(commodity.getName());
        commodityData.setInterval_column(commodity.getInterval_column());
        commodityData.setUnit(commodity.getUnit());
        commodityData.setCommodityType(CommodityType.valueOf(commodity.getCOMMODITY_TYPE()));

        // Sprawdź czy value_column to "." i usuń rekord, jeśli tak
        if (!".".equals(commodity.getInterval_column())) {
            commodityData.setData(commodity.getData());
        }

        return commodityData;
    }



}
