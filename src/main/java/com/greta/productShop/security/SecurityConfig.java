package com.greta.productShop.security;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
    public class SecurityConfig {
    private final Filter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/user/add").permitAll()
                        .requestMatchers("/product/all").permitAll()
                        .requestMatchers("/product/name/{name}").permitAll()
                        .requestMatchers("/product/collection/{collectionId}").permitAll()
                        .requestMatchers("/user/{id}").hasRole("USER")
                        .requestMatchers("/user/me").hasRole("USER")
                        .requestMatchers("/user/email/{email}").hasRole("USER")
                        .requestMatchers("/stock/decrease").hasRole("USER")
                        .requestMatchers("/stock/increase").hasRole("USER")
                        .requestMatchers("/user/update/{id}").hasRole("USER")
                        .requestMatchers("/orders/add").hasRole("USER")
                        .requestMatchers("/orders/{id}").hasRole("USER")
                        .requestMatchers("/orders/update/{id}").hasRole("USER")
                        .requestMatchers("/orders/delete/{id}").hasRole("USER")
                        .requestMatchers("/order-products/add").hasRole("USER")
                        .requestMatchers("/order-products/update/{orderId}/{productId}").hasRole("USER")
                        .requestMatchers("/order-products/delete/{orderId}/{productId}").hasRole("USER")
                        .requestMatchers("/order-products/{id}").hasRole("USER")
                        .requestMatchers("/order-products/{id}").hasRole("USER")
                        .requestMatchers("/invoice/generate").hasRole("USER")
                        .requestMatchers("/invoice/get/{orderId}").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/all").hasRole("ADMIN")
                        .requestMatchers("/orders/all").hasRole("ADMIN")
                        .requestMatchers("/order-products/all").hasRole("ADMIN")
                        .requestMatchers("/product/add").hasRole("ADMIN")
                        .requestMatchers("/product/update/{id}").hasRole("ADMIN")
                        .requestMatchers("/product/delete").hasRole("ADMIN")
                        .requestMatchers("/stock/check").hasRole("ADMIN")
                        .anyRequest().authenticated()
            );
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
    

