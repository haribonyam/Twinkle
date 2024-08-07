package com.example.twinkle.common.config.auth;

import com.example.twinkle.service.socialLogin.SocialLoginService;
import com.example.twinkle.service.socialLogin.SocialLoginSuccessHandler;
import com.example.twinkle.service.user.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final AuthenticationConfiguration authenticationConfiguration;
        private final JwtUtil jwtUtil;
        private final RefreshTokenService refreshTokenService;
        private final DefaultOAuth2UserService socialLoginService;
        private final SocialLoginSuccessHandler socialLoginSuccessHandler;

//        @Bean
//        public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
//
//            return configuration.getAuthenticationManager();
//        }
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
//                    @Override
//                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                        CorsConfiguration configuration = new CorsConfiguration();
//                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
//                        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//                        configuration.setAllowCredentials(true);
//                        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
//                        configuration.setExposedHeaders(Arrays.asList("Authorization", "Nickname", "Id"));
//                        configuration.setMaxAge(3600L);
//                        return configuration;
//                    }
//                }))
//                .csrf(csrf -> csrf.disable())
//                .formLogin(formLogin -> formLogin.disable())
//                .httpBasic(httpBasic -> httpBasic.disable())
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/oauth2/**", "/social").permitAll()
//                        .requestMatchers("/", "/login","/mailConfirm","/api/user/save", "/api/user/check/**").permitAll()
//                        .requestMatchers("/token/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth2 -> oauth2
//                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/code/*"))
//                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(socialLoginService))
//                        .successHandler(socialLoginSuccessHandler)
//                )
//                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
//                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshTokenService), UsernamePasswordAuthenticationFilter.class)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        return http.build();
//    }
//
//
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> {
//            web.ignoring()
//                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//        };
//    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/oauth2/**", "/social").permitAll()
                        .requestMatchers("/", "/login", "/mailConfirm", "/api/user/save", "/api/user/check/**").permitAll()
                        .requestMatchers("/token/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/code/*"))
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(socialLoginService))
                        .successHandler(socialLoginSuccessHandler)
                )
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(), jwtUtil, refreshTokenService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Nickname", "Id"));
        configuration.setMaxAge(3600L);
        return request -> configuration;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }



    }


