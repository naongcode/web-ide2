package com.example.myapp.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                            .anyRequest().permitAll() // ✅ 모든 요청 허용
//                        .requestMatchers("/ws/**").permitAll()  // WebSocket 허용
//                        .requestMatchers("/api/chat/search").permitAll()  // ✅ 검색 API도 허용
//                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // ⭐ Swagger 경로 허용
//                        .requestMatchers("/quest/**").permitAll()
//                        .anyRequest().authenticated()          // 나머지는 인증 필요
                )
                .csrf(csrf -> csrf
                        .disable()  // WebSocket은 CSRF 제외
                );

        return http.build();
    }
}
