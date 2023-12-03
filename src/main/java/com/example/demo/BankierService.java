package com.example.demo;


import com.example.demo.Commodity.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BankierService {

    private final String BANKIER_URL = "https://www.bankier.pl/new-charts/get-data?symbol=";

    @Autowired
    private CommodityRepository commodityRepository;

    @Autowired
    private DataPointRepository dataPointRepository;
    @PostConstruct
    public void initializeData() {
        fetchDataAndSave();
    }
    public void fetchDataAndSave() {
        LocalDate currentDate = LocalDate.now();
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        for (CommodityType commodityType : CommodityType.values()) {
            Optional<CommodityEntity> existingCommodityOptional = commodityRepository.findByName(commodityType.name());

            if (existingCommodityOptional.isPresent()) {
                CommodityEntity existingCommodity = existingCommodityOptional.get();
                if (!isDataUpToDate(existingCommodity)) {
                    fetchAndSaveData(commodityType, existingCommodity);
                } else {
                    System.out.println("Dane dla surowca " + commodityType + " są aktualne.");
                }
            } else {
                fetchAndSaveData(commodityType, null);
            }
        }

    }
    private boolean isWorkingDay() {
        LocalDate currentDate = LocalDate.now();
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

        // Załóżmy, że poniedziałek-piątek są dniami roboczymi
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
    private void fetchAndSaveData(CommodityType commodityType, CommodityEntity existingCommodity) {

        String fullUrl = buildUrlForCommodity(commodityType);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity(fullUrl, ApiResponse.class);

        ApiResponse apiResponse = response.getBody();

        if (apiResponse != null && !apiResponse.getMain().isEmpty()) {
            CommodityEntity commodityEntity;

            if (existingCommodity != null) {
                commodityEntity = existingCommodity;
            } else {
                commodityEntity = new CommodityEntity();
                commodityEntity.setName(commodityType.name());
            }

            CommodityEntity savedCommodity = commodityRepository.save(commodityEntity);
            List<DataPointEntity> dataPoints = new ArrayList<>();

            Date lastSavedDataTimestamp = getLastSavedDataTimestamp(savedCommodity);

            for (List<Number> dataPoint : apiResponse.getMain()) {
                Date dataPointTimestamp = new Date((long) dataPoint.get(0));
                // Sprawdź, czy data punktu danych jest nowsza niż ostatni zapisany punkt danych
                if (dataPointTimestamp.after(lastSavedDataTimestamp)) {
                    // Sprawdź, czy dzisiaj jest dniem roboczym
                    if (isWorkingDay()) {
                        DataPointEntity dataPointEntity = new DataPointEntity();
                        dataPointEntity.setCommodity(savedCommodity);
                        dataPointEntity.setTimestamp(dataPointTimestamp);
                        dataPointEntity.setValue(((Number) dataPoint.get(1)).doubleValue());
                        dataPoints.add(dataPointEntity);
                        System.out.println("Dodano nowy punkt danych: " + dataPointEntity);
                    }
                }
            }

            // Zapisz tylko nowe punkty danych
            dataPointRepository.saveAll(dataPoints);

            // Uruchom skrypt tylko, jeśli dodano nowe punkty danych
            if (!dataPoints.isEmpty()) {
                System.out.println("Uruchamiam skrypt");
                runPythonScript();
            }
        }
    }


    private Date getLastSavedDataTimestamp(CommodityEntity commodity) {
        DataPointEntity lastSavedDataPoint = dataPointRepository.findTopByCommodityOrderByTimestampDesc(Optional.ofNullable(commodity));
        return lastSavedDataPoint != null ? lastSavedDataPoint.getTimestamp() : new Date(0);
    }

    private boolean isDataUpToDate(CommodityEntity commodity) {
        DataPointEntity latestDataPoint = dataPointRepository.findTopByCommodityOrderByTimestampDesc(Optional.ofNullable(commodity));

        return latestDataPoint != null && isToday(latestDataPoint.getTimestamp());
    }


    private boolean isToday(Date date) {
        LocalDate currentDate = LocalDate.now();
        LocalDate dataPointDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return currentDate.isEqual(dataPointDate);
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
    private String buildUrlForCommodity(CommodityType commodityType) {
        return BANKIER_URL + commodityType.name() + "&intraday=false&type=area&max_period=true";
    }
}




