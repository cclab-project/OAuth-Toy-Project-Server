package com.cclab.oauth.config;


import com.cclab.oauth.commom.exhandler.JwtAccessDeniedHandler;
import com.cclab.oauth.domain.jwt.filter.JwtAuthenticationFilter;
import com.cclab.oauth.domain.jwt.service.TokenService;
import com.cclab.oauth.security.UserDetailsServiceImpl;
import com.cclab.oauth.security.entrypoint.JwtAuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        return http
                .httpBasic(AbstractHttpConfigurer::disable) //rest api를 사용하므로 비활성화
                .csrf(AbstractHttpConfigurer::disable)  // token을 사용하므로 비활성화
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer -> {
                    configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(request -> {
                    request.anyRequest().permitAll();
                })
                .addFilterBefore(new JwtAuthenticationFilter(userDetailsService, tokenService) , UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
                .exceptionHandling(configurer -> {
                    configurer.accessDeniedHandler(new JwtAccessDeniedHandler(objectMapper));
                    configurer.authenticationEntryPoint(new JwtAuthenticationEntryPoint(objectMapper));
                })
                .build();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
