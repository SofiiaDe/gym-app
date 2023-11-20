package com.xstack.gymapp.service;

import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.model.entity.User;
import com.xstack.gymapp.model.payload.PasswordChangeRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

  String generateUsername(String firstName, String lastName);

  String generatePassword();

  UserDto createUser(UserDto userDto);

  boolean authenticateUser(String username, String password);

  UserDto getUserByUsername(String username);
  
}
