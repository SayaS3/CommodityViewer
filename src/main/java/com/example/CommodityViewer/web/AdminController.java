package com.example.CommodityViewer.web;

import com.example.CommodityViewer.Commodity.CommodityType;
import com.example.CommodityViewer.user.User;
import com.example.CommodityViewer.user.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String getAdminPage(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(required = false) String search,
                               Model model) {
        List<String> commodityTypes = Arrays.stream(CommodityType.values())
                .map(CommodityType::name)
                .collect(Collectors.toList());

        Page<User> usersPage;

        if (StringUtils.isNotBlank(search)) {
            usersPage = userService.searchUsers(search, PageRequest.of(Math.max(page, 0), 11));
        } else {
            usersPage = userService.getAllUsers(PageRequest.of(Math.max(page, 0), 11));
        }

        List<User> users = usersPage.getContent();

        model.addAttribute("users", users);
        model.addAttribute("commodityTypes", commodityTypes);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", usersPage.getTotalPages());

        return "admin";
    }

    @PostMapping("/admin")
    public String deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin";
    }
}