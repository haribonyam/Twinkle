package com.example.twinkle.common.config.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final AuthenticationConfiguration authenticationConfiguration;
        private final JwtUtil jwtUtil;

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{

            return configuration.getAuthenticationManager();
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder(){

            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
            //CORS 나중에 cloud gateway에서 처리
            /*
            http
                    .cors((cors)-> cors
                            .configurationSource(new CorsConfigurationSource() {
                                @Override
                                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                    CorsConfiguration configuration = new CorsConfiguration();
                                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                    configuration.setAllowedMethods(Collections.singletonList("*"));
                                    configuration.setAllowCredentials(true);
                                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                                    configuration.setMaxAge(3600L);

                                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                                    configuration.setExposedHeaders(Collections.singletonList("Nickname"));
                                    return configuration;
                                }
                            }));


             */
            http
                    .csrf((auth) -> auth.disable());

            http
                    .formLogin((auth) -> auth.disable());

            http
                    .httpBasic((auth) -> auth.disable());

            http
                    .authorizeHttpRequests((auth) -> auth
                            .requestMatchers("/login", "/", "/api/user/save","/api/user/check/**").permitAll()
                            .anyRequest().authenticated());

            //JWTFilter 등록

            http
                    .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

            http
                    .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

            http
                    .sessionManagement((session) -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            return http.build();
        }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization", "Nickname") // 여기서 모든 노출할 헤더들을 한 번에 설정
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }

    }


