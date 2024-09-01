package com.example.techlog.config;

import com.example.techlog.auth.error.JwtAccessDeniedHandler;
import com.example.techlog.auth.error.JwtAuthenticationEntryPoint;
import com.example.techlog.auth.filter.JwtAuthenticationProcessingFilter;
import com.example.techlog.auth.login.LoginService;
import com.example.techlog.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final LoginService loginService;
    private final JwtService jwtService;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer<HttpSecurity>::disable)
                .formLogin(FormLoginConfigurer<HttpSecurity>::disable)
                .httpBasic(HttpBasicConfigurer<HttpSecurity>::disable)
                .sessionManagement (it ->
                        it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(it ->
                        it
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/users/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/posts/**").permitAll()
                                .requestMatchers("/error/**").permitAll()
                                .requestMatchers("/errors/**").permitAll()
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService);
    }

}
