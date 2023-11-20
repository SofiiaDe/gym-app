package com.xstack.gymapp.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import com.xstack.gymapp.security.AuthEntryPointJwt;
import com.xstack.gymapp.security.JwtAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;
  private final AuthEntryPointJwt unauthorizedHandler;
  private final AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain libraryFilterChain(HttpSecurity http) throws Exception {
    return http
        .cors(cors -> cors.configure(http))
        .csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(POST, "/api/trainees", "/api/trainers")
            .permitAll()
            .requestMatchers(POST, "/api/auth/login").permitAll()
            .requestMatchers(PUT, "/api/auth/change-password").authenticated()
            .requestMatchers(GET, "/api/trainees/profile").authenticated()
            .requestMatchers(PUT, "/api/trainees", "/api/trainees/update-trainers")
            .authenticated()
            .requestMatchers(DELETE, "/api/trainees").authenticated()
            .requestMatchers(PATCH, "/api/trainees/active-status").authenticated()
            .requestMatchers(GET, "/api/trainers/profile", "/api/trainers/info").authenticated()
            .requestMatchers(PUT, "/api/trainers").authenticated()
            .requestMatchers(PATCH, "/api/trainers/active-status").authenticated()
            .requestMatchers(GET, "/api/trainings/trainee/list", "/api/trainings/unassigned-trainers")
            .authenticated()
            .requestMatchers(GET, "/api/trainings/trainer/list").authenticated()
            .requestMatchers(POST, "/api/trainings/add").authenticated()
            .requestMatchers(DELETE, "/api/trainings").authenticated()
            .anyRequest().authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout.logoutUrl("/api/auth/logout")
            .logoutSuccessHandler(
                (request, response, authentication) -> SecurityContextHolder.clearContext()))
        .build();
  }

}
