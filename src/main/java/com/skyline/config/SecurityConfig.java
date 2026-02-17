package com.skyline.config;

import com.skyline.exception.InvalidTokenException;
import com.skyline.exception.UnauthorizedTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    // Registers your CustomAuthenticationProvider and Replaces default authentication logic
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       CustomAuthenticationProvider customAuthenticationProvider) {

        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        builder.authenticationProvider(customAuthenticationProvider);

        return builder.build();
    }

    // Encrypts passwords securely
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) {

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/public/**").permitAll()
                        .requestMatchers("/api/users/sign-up").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/bookings/**").permitAll()


                        .anyRequest().authenticated()
                ).exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            Throwable cause = authException.getCause();
                            if (cause instanceof InvalidTokenException) {
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                response.getWriter().write("Invalid Token: " + cause.getMessage());
                            } else if (cause instanceof UnauthorizedTokenException) {
                                response.setStatus(HttpStatus.FORBIDDEN.value());
                                response.getWriter().write("Unauthorized Token: " + cause.getMessage());
                            } else {
                                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                                response.getWriter().write("Authentication Failed: " + authException.getMessage());
                            }
                        })
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // JWT filter runs before default security filter

        return http.build();
    }
}
