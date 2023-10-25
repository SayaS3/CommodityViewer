package com.example.demo;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/commodity")
public class CommodityController {

    @Autowired
    private CommodityDataService commodityService;

    @GetMapping("/{commodityType}")
    public String getCommodityPage(@PathVariable CommodityType commodityType, Model model) {
        List<String> commodityTypes = Arrays.asList("COPPER", "ALUMINUM", "WHEAT", "NATURAL_GAS", "BRENT");

        commodityService.findByType(commodityType).ifPresent(commodity -> {
            model.addAttribute("selectedCommodity", commodity);
        });

        model.addAttribute("commodityTypes", commodityTypes);

        return "commodity";
    }
}



