package com.find.doongji.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.find.doongji.auth.enums.Role;
import com.find.doongji.auth.service.CustomUserDetailsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new SecurityContextPersistenceFilter(), BasicAuthenticationFilter.class)
                .csrf((csrfConfig) -> csrfConfig.disable())
                .headers((headerConfig) -> headerConfig.frameOptions((frameOptions) -> frameOptions.disable()))
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                // auth
                                .requestMatchers("/api/v1/auth/login").permitAll()
                                .requestMatchers("/api/v1/auth/logout").authenticated()

                                // member
                                .requestMatchers("/api/v1/member").permitAll()
                                .requestMatchers("/api/v1/member/my").authenticated()

                                // history
                                .requestMatchers("/api/v1/history/**").authenticated()

                                // search
                                .requestMatchers("/api/v1/search/**").permitAll()

                                // TODO: Require SELLER role for listing endpoints.
                                .requestMatchers("/api/v1/listing/**").hasRole("SELLER")

                                // everything else
                                .anyRequest().permitAll()
                )
                .sessionManagement((sessionConfig) ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .formLogin(form -> form.disable()) // Disable default form login


                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint).accessDeniedHandler(accessDeniedHandler)
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    private final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {
                ErrorResponse fail = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Spring security unauthorized...");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                String json = new ObjectMapper().writeValueAsString(fail);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    private final AccessDeniedHandler accessDeniedHandler =
            (request, response, accessDeniedException) -> {
                ErrorResponse fail = new ErrorResponse(HttpStatus.FORBIDDEN, "Spring security forbidden...");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                String json = new ObjectMapper().writeValueAsString(fail);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    @Getter
    @RequiredArgsConstructor
    public class ErrorResponse {

        private final HttpStatus status;
        private final String message;
    }
}
