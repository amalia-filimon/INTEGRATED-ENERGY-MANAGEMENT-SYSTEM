package com.example.monitoring_communication_service.JWTSecurityConfiguration;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(new RestEndpointRequestMatcher()).permitAll()
                                .requestMatchers(new MvcEndpointRequestMatcher()).authenticated()
                                .anyRequest().authenticated()
                );
        return http.build();
    }

    private static class RestEndpointRequestMatcher implements RequestMatcher {
        @Override
        public boolean matches(HttpServletRequest request) {
            return request.getRequestURI().startsWith("/api/v1/auth/");
        }
    }

    private static class MvcEndpointRequestMatcher implements RequestMatcher {

        @Override
        public boolean matches(HttpServletRequest request) {
            return request.getRequestURI().startsWith("/mvc/");
        }
    }
}