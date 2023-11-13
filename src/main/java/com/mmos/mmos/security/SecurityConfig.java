package com.mmos.mmos.security;

import com.mmos.mmos.src.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP 기본 인증 비활성화
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 보안 비활성화
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(STATELESS))   // 세션 비활성화
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers(new AntPathRequestMatcher("/login/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/dev/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/signup/**")).permitAll()
                                .anyRequest().authenticated()
                )
                .logout(logoutConfig -> logoutConfig
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .addLogoutHandler(authService)
                        .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext())))
                .build();
    }
}
