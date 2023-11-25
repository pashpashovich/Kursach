package com.example.bank.config;

import com.example.bank.controller.OurUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/reg").permitAll()
                                .requestMatchers("static/css/**").permitAll()
                                .requestMatchers("static/img/**").permitAll()
                                .requestMatchers("/auth").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth")
                        .loginProcessingUrl("/procces_login")
                        .successHandler((request, response, authentication) -> {
                            if (authentication.getPrincipal() instanceof OurUserDetails) {
                                Long id = ((OurUserDetails) authentication.getPrincipal()).getId();
                                String targetUrl = "/customers/" + id;
                                response.sendRedirect(targetUrl);
                            } else {
                                // Обработка ошибки
                                response.sendRedirect("/error");
                            }
                        })
                        .failureUrl("/failure")

                ).logout(logout ->
                        logout.permitAll()
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/auth/login"));

        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new OurUserInfoUserDetailsService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
