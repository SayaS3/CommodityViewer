package com.example.CommodityViewer;


import com.example.CommodityViewer.Commodity.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DataHandler {

    private final String BANKIER_URL = "https://www.bankier.pl/new-charts/get-data?symbol=";

    @Autowired
    private CommodityRepository commodityRepository;

    @Autowired
    private DataPointRepository dataPointRepository;

    @PostConstruct
    public void initializeData() {
        try {
            checkAndUpdateDataAndRunScript();
        } catch (Exception e) {
            // Obsługa wyjątku, na przykład logowanie błędu
            System.err.println("Błąd podczas pobierania danych: " + e.getMessage());

        }
    }

    public void checkAndUpdateDataAndRunScript() {
        boolean hasNewData = false;

        for (CommodityType commodityType : CommodityType.values()) {
            Optional<CommodityEntity> existingCommodityOptional = commodityRepository.findByName(commodityType.name());

            if (existingCommodityOptional.isPresent()) {
                CommodityEntity existingCommodity = existingCommodityOptional.get();
                if (!isDataUpToDate(existingCommodity) && isWorkingDay()) {
                    fetchAndSaveData(commodityType, existingCommodity);
                    hasNewData = true;
                } else {
                    System.out.println("Dane dla surowca " + commodityType + " są aktualne.");
                }
            } else {
                fetchAndSaveData(commodityType, null);
                hasNewData = true;
            }
        }

        if (hasNewData) {
            System.out.println("Uruchamiam skrypt i obliczam dane, moze to chwile potrwac...");
            runPythonScript();
        } else {
            System.out.println("Brak nowych danych dla wszystkich surowców.");
        }
    }

    private void fetchAndSaveData(CommodityType commodityType, CommodityEntity existingCommodity) {
        String fullUrl = buildUrlForCommodity(commodityType);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity(fullUrl, ApiResponse.class);
        ApiResponse apiResponse = response.getBody();
        List<DataPointEntity> dataPoints = new ArrayList<>();
        if (apiResponse != null && !apiResponse.getMain().isEmpty()) {
            CommodityEntity savedCommodity = saveOrUpdateCommodity(commodityType, existingCommodity);

            Date lastSavedDataTimestamp = getLastSavedDataTimestamp(savedCommodity);

            for (List<Number> dataPoint : apiResponse.getMain()) {
                Date dataPointTimestamp = new Date((long) dataPoint.get(0));

                if (dataPointTimestamp.after(lastSavedDataTimestamp) && (isWorkingDay() || isDatabaseEmpty(savedCommodity))) {
                    DataPointEntity dataPointEntity = createDataPointEntity(savedCommodity, dataPointTimestamp, dataPoint);
                    dataPoints.add(dataPointEntity);
                    System.out.println("Dodano nowy punkt danych: " + dataPointEntity);
                }
            }

            dataPointRepository.saveAll(dataPoints);
        }

    }


    private CommodityEntity saveOrUpdateCommodity(CommodityType commodityType, CommodityEntity existingCommodity) {
        CommodityEntity commodityEntity = existingCommodity != null ? existingCommodity : new CommodityEntity();
        commodityEntity.setName(commodityType.name());
        return commodityRepository.save(commodityEntity);
    }

    private DataPointEntity createDataPointEntity(CommodityEntity savedCommodity, Date dataPointTimestamp, List<Number> dataPoint) {
        DataPointEntity dataPointEntity = new DataPointEntity();
        dataPointEntity.setCommodity(savedCommodity);
        dataPointEntity.setTimestamp(dataPointTimestamp);
        dataPointEntity.setValue(dataPoint.get(1).doubleValue());
        return dataPointEntity;
    }
    private boolean isWorkingDay() {
        LocalDate currentDate = LocalDate.now();
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
    private boolean isDatabaseEmpty(CommodityEntity commodityEntity) {
        List<DataPointEntity> existingDataPoints = dataPointRepository.findByCommodity(commodityEntity);
        return existingDataPoints.isEmpty();
    }

    private Date getLastSavedDataTimestamp(CommodityEntity commodity) {
        DataPointEntity lastSavedDataPoint = dataPointRepository.findTopByCommodityOrderByTimestampDesc(Optional.ofNullable(commodity));
        return lastSavedDataPoint != null ? lastSavedDataPoint.getTimestamp() : new Date(0);
    }

    private boolean isDataUpToDate(CommodityEntity commodity) {
        DataPointEntity latestDataPoint = dataPointRepository.findTopByCommodityOrderByTimestampDesc(Optional.ofNullable(commodity));
        return latestDataPoint != null && isAfterOrEqualYesterday(latestDataPoint.getTimestamp());
    }


    private boolean isAfterOrEqualYesterday(Date date) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate dataPointDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return !dataPointDate.isBefore(yesterday);
    }

    public void runPythonScript() {
        try {
            File scriptFile = new File("src/main/resources/python/main.py");
            if (scriptFile.exists()) {
                File outputLogFile = new File("target/classes/python/output.log");
                ProcessBuilder processBuilder = new ProcessBuilder("python", scriptFile.getAbsolutePath());
                processBuilder.redirectErrorStream(true);
                processBuilder.redirectOutput(outputLogFile);
                Process process = processBuilder.start();
                process.waitFor();
                System.out.println("Skrypt wykonany");
            } else {
                System.out.println("Plik skryptu nie istnieje w lokalizacji: " + scriptFile.getAbsolutePath());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private String buildUrlForCommodity(CommodityType commodityType) {
        return BANKIER_URL + commodityType.name() + "&intraday=false&type=area&max_period=true";
    }
}





