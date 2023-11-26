package com.example.demo.Commodity;

import com.example.demo.ADF.AdfResult;
import com.example.demo.ADF.AdfResultService;
import com.example.demo.FORECAST.Forecast;
import com.example.demo.FORECAST.ForecastService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/")
public class CommodityController {

    @Autowired
    private CommodityDataService commodityService;

    @Autowired
    private ForecastService forecastService;

    @Autowired
    private AdfResultService adfResultService;

    @GetMapping
    public String getHomePage(Model model) {
        List<String> commodityTypes = Arrays.asList("COPPER", "ALUMINUM", "WHEAT", "NATURAL_GAS", "BRENT");
        model.addAttribute("commodityTypes", commodityTypes);
        model.addAttribute("selectedCommodity", commodityTypes);

        return "home";
    }

    @GetMapping("/{commodityType:[A-Za-z_]+}")
    public String getCleanDataPage(@PathVariable CommodityType commodityType, Model model) {
        List<String> commodityTypes = Arrays.asList("COPPER", "ALUMINUM", "WHEAT", "NATURAL_GAS", "BRENT");

        List<String> dates = new ArrayList<>();
        List<Double> values = new ArrayList<>();

        commodityService.findByType(commodityType).ifPresent(commodity -> {
            model.addAttribute("selectedCommodity", commodity);

            // Uzupełnij listy danymi z data_point
            for (DataPoint dataPoint : commodity.getData()) {
                dates.add(dataPoint.getDate());
                values.add(Double.valueOf(dataPoint.getValue_column()));
            }
        });

        model.addAttribute("dates", dates);
        model.addAttribute("values", values);
        model.addAttribute("commodityTypes", commodityTypes);
        return "data";
    }

    @GetMapping("/{commodityType}/testadf")
    public String getTestADFPage(@PathVariable CommodityType commodityType, Model model) {
        List<String> commodityTypes = Arrays.asList("COPPER", "ALUMINUM", "WHEAT", "NATURAL_GAS", "BRENT");

        commodityService.findByType(commodityType).ifPresent(commodity -> model.addAttribute("selectedCommodity", commodity));

        CommodityData selectedCommodity = commodityService.findByType(commodityType).orElse(null);

        if (selectedCommodity != null) {
            commodityService.findByType(commodityType).ifPresent(commodity -> model.addAttribute("selectedCommodity", commodity));
            List<AdfResult> adfResults = adfResultService.getAdfResultsByCommodityId(selectedCommodity.getId());
            model.addAttribute("commodityTypes", commodityTypes);
            model.addAttribute("adfResults", adfResults);
            model.addAttribute("selectedCommodity", selectedCommodity);
            return "testadf";
        } else {
            // Obsługa sytuacji, gdy nie znaleziono wybranego surowca
            return "error"; // Przekierowanie na stronę główną lub inny odpowiedni adres
        }
    }


    @GetMapping("/{commodityType}/forecasts")
    public String getForecastsPage(@PathVariable CommodityType commodityType, Model model) {
        List<String> commodityTypes = Arrays.asList("COPPER", "ALUMINUM", "WHEAT", "NATURAL_GAS", "BRENT");

        commodityService.findByType(commodityType).ifPresent(commodity -> model.addAttribute("selectedCommodity", commodity));

        CommodityData selectedCommodity = commodityService.findByType(commodityType).orElse(null);

        List<String> dates = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        List<String> forecastDates = new ArrayList<>();  // Dodaj listę dla dat prognoz
        List<Double> forecastValues = new ArrayList<>();  // Dodaj listę dla wartości prognoz

        commodityService.findByType(commodityType).ifPresent(commodity -> {
            model.addAttribute("selectedCommodity", commodity);

            // Uzupełnij listy danymi z data_point
            for (DataPoint dataPoint : commodity.getData()) {
                dates.add(dataPoint.getDate());
                values.add(Double.valueOf(dataPoint.getValue_column()));
            }
        });

        if (selectedCommodity != null) {
            commodityService.findByType(commodityType).ifPresent(commodity -> model.addAttribute("selectedCommodity", commodity));

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
            return "forecasts";
        } else {
            return "error"; // Przekierowanie na stronę główną lub inny odpowiedni adres
        }
    }



}



