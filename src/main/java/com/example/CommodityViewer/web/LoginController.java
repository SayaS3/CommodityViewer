package com.example.CommodityViewer.web;

import com.example.CommodityViewer.Commodity.CommodityType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm(Model model) {
        List<String> commodityTypes =  Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());
        model.addAttribute("commodityTypes", commodityTypes);
        model.addAttribute("selectedCommodity", commodityTypes);
        return "login-form";
    }
}