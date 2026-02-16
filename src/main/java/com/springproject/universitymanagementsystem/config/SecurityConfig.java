package com.springproject.universitymanagementsystem.config;

import com.springproject.universitymanagementsystem.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()

                // Course delete - only TEACHER can delete
                .requestMatchers(HttpMethod.DELETE, "/api/courses/**").hasRole("TEACHER")

                // Student endpoints
                // Teachers can do everything with students
                .requestMatchers(HttpMethod.DELETE, "/api/students/**").hasRole("TEACHER")
                .requestMatchers(HttpMethod.POST, "/api/students/**").hasRole("TEACHER")

                // Students can update their own info via /self endpoint
                .requestMatchers(HttpMethod.PUT, "/api/students/*/self").hasAnyRole("STUDENT", "TEACHER")

                // Both can read
                .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("STUDENT", "TEACHER")

                // Teachers have full access to all endpoints
                .requestMatchers("/api/**").hasRole("TEACHER")

                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {})
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
