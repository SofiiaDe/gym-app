package com.xstack.gymapp.service;

import com.xstack.gymapp.model.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

  String extractUsername(String token);

  boolean isTokenValid(String token, UserDetails userDetails);

  String generateToken(UserDto userDto);

  String generateRefreshToken(UserDto userDto);

}
