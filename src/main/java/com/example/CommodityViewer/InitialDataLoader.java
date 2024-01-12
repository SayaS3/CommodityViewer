package com.example.CommodityViewer;

import com.example.CommodityViewer.user.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader {

    @Autowired
    private final UserRoleRepository userRoleRepository;

    @Autowired
    private final UserService userService;
    public InitialDataLoader(UserRoleRepository userRoleRepository, UserRepository userRepository, UserService userService) {
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
    }


    @PostConstruct
    public void init() {
        // Sprawdzamy, czy role są już w bazie
        if (userRoleRepository.count() == 0) {
            userRoleRepository.save(new UserRole("ADMIN", "ma dostęp do panelu admina, zarządza użytkownikami"));
            userRoleRepository.save(new UserRole("USER", "ma dostęp do analizy cen surowców"));
            userService.registerAdmin();
        }

    }

}