package com.xstack.gymapp.security;

import com.xstack.gymapp.exception.JwtAuthException;
import com.xstack.gymapp.repository.TokenRepository;
import com.xstack.gymapp.service.JwtService;
import com.xstack.gymapp.service.impl.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Log4j2
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private static final String CAN_NOT_AUTHENTICATE = "Cannot set user authentication: ";

  private final JwtService jwtService;
  private final TokenRepository tokenRepository;
  private final CustomUserDetailsService customUserDetailsService;


  @Override
  public void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {

    try {
      if (request.getServletPath().contains("/api/v1/auth")) {
        filterChain.doFilter(request, response);
        return;
      }
      final String authHeader = request.getHeader("Authorization");
      final String jwt;
      final String email;
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
      }
      jwt = authHeader.substring(7);
      email = jwtService.extractUsername(jwt);
      if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        var isTokenValid = tokenRepository.findByToken(jwt)
            .map(token -> !token.isExpired() && !token.isRevoked())
            .orElse(false);
        if (jwtService.isTokenValid(jwt, userDetails) && Boolean.TRUE.equals(isTokenValid)) {
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(userDetails, null,
                  userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (Exception e) {
      log.error(CAN_NOT_AUTHENTICATE + e);
      throw new JwtAuthException(CAN_NOT_AUTHENTICATE + e);
    }

    filterChain.doFilter(request, response);
  }

}