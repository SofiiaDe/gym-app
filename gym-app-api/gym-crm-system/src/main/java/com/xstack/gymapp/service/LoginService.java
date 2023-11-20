package com.xstack.gymapp.service;

import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.LoginResponse;
import com.xstack.gymapp.model.payload.PasswordChangeRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface LoginService {

  LoginResponse login(LoginRequest loginRequest);

  boolean changePassword(PasswordChangeRequest request);

  void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication);

  String getNewAuthTokenIfPasswordChanged(String oldToken, PasswordChangeRequest request);
}
