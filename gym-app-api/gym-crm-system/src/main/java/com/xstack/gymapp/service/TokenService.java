package com.xstack.gymapp.service;

import com.xstack.gymapp.model.dto.UserDto;

public interface TokenService {

  void saveUserToken(UserDto user, String jwtToken);

  void revokeAllUserTokens(Long userId);

  boolean updateTokenAfterLogout(String jwt);

  String getValidJWTTokenByUserId(Long userId);
}
