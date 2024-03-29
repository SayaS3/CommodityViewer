package com.example.CommodityViewer.config.security;

import com.example.CommodityViewer.Commodity.CommodityType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class CustomSecurityConfig {
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> {
                    authorize
                            .requestMatchers(mvc.pattern("/admin/**"))
                            .hasAnyRole(ADMIN_ROLE);
                    authorize
                            .requestMatchers(mvc.pattern("/correlations/**"))
                            .hasAnyRole(USER_ROLE, ADMIN_ROLE);
                    for (CommodityType commodity : CommodityType.values()) {
                        authorize
                                .requestMatchers(mvc.pattern("/" + commodity.name()))
                                .permitAll()
                                .requestMatchers(mvc.pattern("/" + commodity.name() + "/**"))
                                .hasAnyRole(USER_ROLE, ADMIN_ROLE);
                    }
                    authorize.anyRequest().permitAll();
                })
                .formLogin(login -> login
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout/**", HttpMethod.GET.name()))
                        .logoutSuccessUrl("/login?logout").permitAll()
                );

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}