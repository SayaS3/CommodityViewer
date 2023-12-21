package com.example.CommodityViewer.Commodity;

import com.example.CommodityViewer.ADF.AdfResult;
import com.example.CommodityViewer.ADF.AdfResultService;
import com.example.CommodityViewer.ARIMA.Arima;
import com.example.CommodityViewer.ARIMA.ArimaService;
import com.example.CommodityViewer.HoltWinters.Forecast;
import com.example.CommodityViewer.HoltWinters.ForecastService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ForecastService forecastService;
    @Autowired
    private ArimaService arimaService;
    @Autowired
    private AdfResultService adfResultService;

    @GetMapping
    public String getHomePage(Model model) {
        List<String> commodityTypes =  Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());
        model.addAttribute("commodityTypes", commodityTypes);
        model.addAttribute("selectedCommodity", commodityTypes);

        return "home";
    }
    @GetMapping("/correlations")
    public String getCorrelation(Model model) {
        List<String> commodityTypes = Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());
        model.addAttribute("commodityTypes", commodityTypes);

        return "correlations";
    }
    @GetMapping("/admin")
    public String getAdminPage(Model model) {
        List<String> commodityTypes = Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());
        model.addAttribute("commodityTypes", commodityTypes);
        return "admin";
    }

    @PostMapping("/correlations")
    public String showCorrelation(@RequestParam("commodity1") String commodity1,
                                  @RequestParam("commodity2") String commodity2,
                                  Model model) {
        List<String> dates = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        List<Double> values2 = new ArrayList<>();
        List<String> commodityTypes = Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());
        CommodityEntity selectedCommodity1 = commodityService.findCommodityByName(commodity1).orElse(null);
        CommodityEntity selectedCommodity2 = commodityService.findCommodityByName(commodity2).orElse(null);

        if (selectedCommodity1 != null && selectedCommodity2 != null) {
            List<DataPointEntity> dataPoints1 = commodityService.getDataPointsForCommodity(selectedCommodity1.getName());
            List<DataPointEntity> dataPoints2 = commodityService.getDataPointsForCommodity(selectedCommodity2.getName());

            // Dopasuj wartości do wspólnych dat
            Map<String, Double> valuesMap1 = dataPoints1.stream()
                    .collect(Collectors.toMap(
                            dp -> String.valueOf(dp.getTimestamp()),
                            DataPointEntity::getValue,
                            (existing, replacement) -> existing // Handle duplicates by keeping the existing value
                    ));

            for (DataPointEntity dataPoint : dataPoints2) {
                String timestamp = String.valueOf(dataPoint.getTimestamp());
                if (valuesMap1.containsKey(timestamp)) {
                    dates.add(timestamp);
                    values.add(valuesMap1.get(timestamp));
                    values2.add(dataPoint.getValue());
                }
            }

            // Dodaj obliczenia współczynników korelacji do modelu
            double pearsonCorrelation = calculatePearsonCorrelation(values, values2);
            double spearmanCorrelation = calculateSpearmanCorrelation(values, values2);
            // Zaokrąglenie do dwóch miejsc po przecinku
            pearsonCorrelation = Math.round(pearsonCorrelation * 100.0) / 100.0;
            spearmanCorrelation = Math.round(spearmanCorrelation * 100.0) / 100.0;
            model.addAttribute("dates", dates);
            model.addAttribute("values", values);
            model.addAttribute("commodity1", selectedCommodity1);
            model.addAttribute("dates2", dates);
            model.addAttribute("values2", values2);
            model.addAttribute("commodity2", selectedCommodity2);
            model.addAttribute("commodityTypes", commodityTypes);
            model.addAttribute("selectedCommodity", commodityTypes);
            model.addAttribute("pearsonCorrelation", pearsonCorrelation);
            model.addAttribute("spearmanCorrelation", spearmanCorrelation);
        }

        return "correlations";
    }


    // Metoda do obliczania współczynnika korelacji Pearsona
    private double calculatePearsonCorrelation(List<Double> values1, List<Double> values2) {
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();

        // Znajdź wspólne daty
        List<Double> commonValues1 = new ArrayList<>();
        List<Double> commonValues2 = new ArrayList<>();
        for (int i = 0; i < values1.size(); i++) {
            if (values2.size() > i) {
                commonValues1.add(values1.get(i));
                commonValues2.add(values2.get(i));
            }
        }

        double[] array1 = commonValues1.stream().mapToDouble(Double::doubleValue).toArray();
        double[] array2 = commonValues2.stream().mapToDouble(Double::doubleValue).toArray();

        return pearsonsCorrelation.correlation(array1, array2);
    }

    // Metoda do obliczania współczynnika korelacji Spearmana
    private double calculateSpearmanCorrelation(List<Double> values1, List<Double> values2) {
        SpearmansCorrelation spearmansCorrelation = new SpearmansCorrelation();

        // Znajdź wspólne daty
        List<Double> commonValues1 = new ArrayList<>();
        List<Double> commonValues2 = new ArrayList<>();
        for (int i = 0; i < values1.size(); i++) {
            if (values2.size() > i) {
                commonValues1.add(values1.get(i));
                commonValues2.add(values2.get(i));
            }
        }

        double[] array1 = commonValues1.stream().mapToDouble(Double::doubleValue).toArray();
        double[] array2 = commonValues2.stream().mapToDouble(Double::doubleValue).toArray();

        return spearmansCorrelation.correlation(array1, array2);
    }
    @GetMapping("/{commodityType:[A-Za-z_]+}")
    public String getDataPage(@PathVariable CommodityType commodityType, Model model) {

        List<String> commodityTypes = Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());

        List<String> dates = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        commodityService.findCommodityByName(commodityType.name()).ifPresent(commodity -> {
            model.addAttribute("selectedCommodity", commodity);

            // Uzupełnij listy danymi z data_point
            List<DataPointEntity> dataPoints = commodityService.getDataPointsForCommodity(commodity.getName());
            for (DataPointEntity dataPoint : dataPoints) {
                dates.add(String.valueOf(dataPoint.getTimestamp()));
                values.add(dataPoint.getValue());
            }

            // Dodaj jednostkę i walutę do modelu w zależności od wybranego surowca
            model.addAttribute("unit", commodityType.getUnit());
            model.addAttribute("currency", commodityType.getCurrency());
        });

        model.addAttribute("dates", dates);
        model.addAttribute("values", values);
        model.addAttribute("commodityTypes", commodityTypes);
        return "data";
    }


    @GetMapping("/{commodityType}/testadf")
    public String getTestADFPage(@PathVariable CommodityType commodityType, Model model) {
        List<String> commodityTypes =  Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());

        commodityService.findCommodityByName(commodityType.name()).ifPresent(commodity -> model.addAttribute("selectedCommodity", commodity));

        CommodityEntity selectedCommodity = commodityService.findCommodityByName(commodityType.name()).orElse(null);

        if (selectedCommodity != null) {
            commodityService.findCommodityByName(commodityType.name()).ifPresent(commodity -> model.addAttribute("selectedCommodity", commodity));
            List<AdfResult> adfResults = adfResultService.getAdfResultsByCommodityId(selectedCommodity.getId());
            model.addAttribute("commodityTypes", commodityTypes);
            model.addAttribute("adfResults", adfResults);
            model.addAttribute("selectedCommodity", selectedCommodity);
            model.addAttribute("unit", commodityType.getUnit());
            model.addAttribute("currency", commodityType.getCurrency());
            return "testadf";
        } else {
            // Obsługa sytuacji, gdy nie znaleziono wybranego surowca
            return "error"; // Przekierowanie na stronę główną lub inny odpowiedni adres
        }
    }


    @GetMapping("/{commodityType}/holtwinters")
    public String getForecastsPage(@PathVariable CommodityType commodityType, Model model) {
        List<String> commodityTypes =  Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());

        commodityService.findCommodityByName(commodityType.name()).ifPresent(commodity -> model.addAttribute("selectedCommodity", commodity));

        CommodityEntity selectedCommodity = commodityService.findCommodityByName(commodityType.name()).orElse(null);

        List<String> dates = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        List<String> forecastDates = new ArrayList<>();  // Dodaj listę dla dat prognoz
        List<Double> forecastValues = new ArrayList<>();  // Dodaj listę dla wartości prognoz

        commodityService.findCommodityByName(commodityType.name()).ifPresent(commodity -> {
            model.addAttribute("selectedCommodity", commodity);

            // Uzupełnij listy danymi z data_point
            List<DataPointEntity> dataPoints = commodityService.getDataPointsForCommodity(commodity.getName());
            for (DataPointEntity dataPoint : dataPoints) {
                dates.add(String.valueOf(dataPoint.getTimestamp()));
                values.add(dataPoint.getValue());
                model.addAttribute("unit", commodityType.getUnit());
                model.addAttribute("currency", commodityType.getCurrency());
            }
        });

        if (selectedCommodity != null) {
            commodityService.findCommodityByName(commodityType.name()).ifPresent(commodity -> model.addAttribute("selectedCommodity", commodity));

            List<Forecast> forecasts = forecastService.getForecastsByCommodityId(selectedCommodity.getId());

            // Konwersja daty na format bez godziny
            forecasts.forEach(forecast -> {
                forecast.setForecastDate(LocalDate.ofEpochDay(forecast.getForecastDate().toEpochDay()));
                forecastDates.add(forecast.getForecastDate().toString());  // Dodaj daty prognoz do listy
                forecastValues.add(forecast.getForecastValue());  // Dodaj wartości prognoz do listy
            });
            model.addAttribute("dates", dates);
            model.addAttribute("values", values);
            model.addAttribute("forecastDates", forecastDates);  // Dodaj prognozowane daty do modelu
            model.addAttribute("forecastValues", forecastValues);  // Dodaj prognozowane wartości do modelu
            model.addAttribute("commodityTypes", CommodityType.values());
            model.addAttribute("forecasts", forecasts);
            model.addAttribute("selectedCommodity", selectedCommodity);
            model.addAttribute("commodityTypes", commodityTypes);
            return "holtwinters";
        } else {
            return "error"; // Przekierowanie na stronę główną lub inny odpowiedni adres
        }
    }
    @GetMapping("/{commodityType}/sarimax")
    public String getArimaPage(@PathVariable CommodityType commodityType, Model model) {
        List<String> commodityTypes =  Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());

        commodityService.findCommodityByName(commodityType.name()).ifPresent(commodity -> model.addAttribute("selectedCommodity", commodity));

        CommodityEntity selectedCommodity = commodityService.findCommodityByName(commodityType.name()).orElse(null);

        List<String> dates = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        List<String> forecastDates = new ArrayList<>();  // Dodaj listę dla dat prognoz
        List<Double> forecastValues = new ArrayList<>();  // Dodaj listę dla wartości prognoz

        commodityService.findCommodityByName(commodityType.name()).ifPresent(commodity -> {
            model.addAttribute("selectedCommodity", commodity);

            // Uzupełnij listy danymi z data_point
            List<DataPointEntity> dataPoints = commodityService.getDataPointsForCommodity(commodity.getName());
            for (DataPointEntity dataPoint : dataPoints) {
                dates.add(String.valueOf(dataPoint.getTimestamp()));
                values.add(dataPoint.getValue());
                model.addAttribute("unit", commodityType.getUnit());
                model.addAttribute("currency", commodityType.getCurrency());
            }
        });

        if (selectedCommodity != null) {
            commodityService.findCommodityByName(commodityType.name()).ifPresent(commodity -> model.addAttribute("selectedCommodity", commodity));

            List<Arima> forecasts = arimaService.getArimabyCommodityId(selectedCommodity.getId());

            // Konwersja daty na format bez godziny
            forecasts.forEach(forecast -> {
                forecast.setForecastDate(LocalDate.ofEpochDay(forecast.getForecastDate().toEpochDay()));
                forecastDates.add(forecast.getForecastDate().toString());  // Dodaj daty prognoz do listy
                forecastValues.add(forecast.getForecastValue());  // Dodaj wartości prognoz do listy
            });
            model.addAttribute("dates", dates);
            model.addAttribute("values", values);
            model.addAttribute("forecastDates", forecastDates);  // Dodaj prognozowane daty do modelu
            model.addAttribute("forecastValues", forecastValues);  // Dodaj prognozowane wartości do modelu
            model.addAttribute("commodityTypes", CommodityType.values());
            model.addAttribute("forecasts", forecasts);
            model.addAttribute("selectedCommodity", selectedCommodity);
            model.addAttribute("commodityTypes", commodityTypes);
            return "sarimax";
        } else {
            return "error"; // Przekierowanie na stronę główną lub inny odpowiedni adres
        }
    }


}


