package com.example.demo.Commodity;

import com.example.demo.ADF.AdfResult;
import com.example.demo.ADF.AdfResultService;
import com.example.demo.FORECAST.Forecast;
import com.example.demo.FORECAST.ForecastService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ForecastService forecastService;

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

    @GetMapping("/{commodityType:[A-Za-z_]+}")
    public String getCleanDataPage(@PathVariable CommodityType commodityType, Model model) {

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


    @GetMapping("/{commodityType}/forecasts")
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
            return "forecasts";
        } else {
            return "error"; // Przekierowanie na stronę główną lub inny odpowiedni adres
        }
    }



}


