package com.example.CommodityViewer.web;

import com.example.CommodityViewer.Commodity.CommodityType;
import com.example.CommodityViewer.user.UserRegistrationDto;
import com.example.CommodityViewer.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        UserRegistrationDto userRegistration = new UserRegistrationDto();
        List<String> commodityTypes =  Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());
        model.addAttribute("commodityTypes", commodityTypes);
        model.addAttribute("selectedCommodity", commodityTypes);
        model.addAttribute("user", userRegistration);
        return "registration-form";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute("user") UserRegistrationDto userRegistration, Model model) {
        List<String> commodityTypes =  Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());
        model.addAttribute("commodityTypes", commodityTypes);
        model.addAttribute("selectedCommodity", commodityTypes);
        model.addAttribute("user", userRegistration);
        model.addAttribute("emailExistsError", "Użytkownik z tym adresem e-mail już istnieje");
        if (userService.existsByEmail(userRegistration.getEmail())) {
            return "registration-form";
        }

        userService.registerUserWithDefaultRole(userRegistration);
        return "redirect:/login";
    }
}