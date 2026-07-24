package com.triage.emergency_triage_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/home", "/login", "/register", "/error",
                                "/css/**", "/js/**", "/img/**"
                        ).permitAll()
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/reports/**").hasRole("NURSE")
                        .requestMatchers("/doctor-reports/**").hasRole("DOCTOR")
                        .requestMatchers("/dashboard", "/patients/**", "/queue/**", "/triage/**", "/settings/**", "/sms/**")
                        .hasAnyRole("ADMIN", "DOCTOR", "NURSE")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/after-login", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}