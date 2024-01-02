package com.xstack.gymapp.trainermicroservice.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

  String extractUsername(String token);

  boolean isTokenValid(String token, UserDetails userDetails);

}
