package com.example.demo;

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
        return "home";
    }

    @GetMapping("/{commodityType}/data")
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

    @GetMapping("/{commodityType:[A-Za-z_]+}")
    public String getCommodityPage(@PathVariable("commodityType") CommodityType commodityType, Model model) {

        List<String> commodityTypes = Arrays.asList("COPPER", "ALUMINUM", "WHEAT", "NATURAL_GAS", "BRENT");
        try {

            commodityService.findByType(commodityType).ifPresent(commodity -> model.addAttribute("selectedCommodity", commodity));
        } catch (IllegalArgumentException e) {
            // Wartość w ścieżce nie jest poprawnym enumem
            return "error"; // Przekieruj na stronę błędu
        }

        model.addAttribute("commodityTypes", commodityTypes);

        return "commodity";
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
        CommodityData selectedCommodity = commodityService.findByType(commodityType).orElse(null);

        if (selectedCommodity != null) {
            commodityService.findByType(commodityType).ifPresent(commodity -> model.addAttribute("selectedCommodity", commodity));

            List<Forecast> forecasts = forecastService.getForecastsByCommodityId(selectedCommodity.getId());

            // Konwersja daty na format bez godziny
            forecasts.forEach(forecast -> forecast.setForecastDate(LocalDate.ofEpochDay(forecast.getForecastDate().toEpochDay())));

            // Ustawienie dostępnych typów surowców
            model.addAttribute("commodityTypes", CommodityType.values());
            model.addAttribute("forecasts", forecasts);
            model.addAttribute("selectedCommodity", selectedCommodity);
            return "forecasts";
        } else {
            // Obsługa sytuacji, gdy nie znaleziono wybranego surowca
            return "error"; // Przekierowanie na stronę główną lub inny odpowiedni adres
        }
    }


}



