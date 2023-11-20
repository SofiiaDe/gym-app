package com.xstack.gymapp.service.impl;

import com.xstack.gymapp.exception.EntityNotFoundException;
import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.model.entity.User;
import com.xstack.gymapp.model.mapper.CycleAvoidingMappingContext;
import com.xstack.gymapp.model.mapper.UserMapper;
import com.xstack.gymapp.model.payload.PasswordChangeRequest;
import com.xstack.gymapp.repository.UserRepository;
import com.xstack.gymapp.service.UserService;
import java.util.Random;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

  private static final String USER_NOT_FOUND_BY_USERNAME = "Cannot find the user with username = ";

  private UserRepository userRepository;
  private UserMapper userMapper;
  private Random random;

  public String generateUsername(String firstName, String lastName) {

    String baseUsername =
        StringUtils.capitalize(firstName) + "." + StringUtils.capitalize(lastName);
    String username = baseUsername;
    int suffix = 1;

    while (userRepository.existsByUsername(username)) {
      username = baseUsername + suffix;
      suffix++;
    }

    log.info("Generated username: {}", username);
    return username;
  }

  public String generatePassword() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder password = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      int index = random.nextInt(characters.length());
      password.append(characters.charAt(index));
    }
    return password.toString();
  }

  public UserDto createUser(UserDto userDto) {
    return userMapper.toDto(userRepository.save(
        userMapper.toEntity(userDto, new CycleAvoidingMappingContext())
    ), new CycleAvoidingMappingContext());
  }

  public boolean authenticateUser(String username, String password) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_BY_USERNAME + username));
    return user != null && user.getPassword().equals(password);
  }

  public UserDto getUserByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_BY_USERNAME + username));
    return userMapper.toDto(user, new CycleAvoidingMappingContext());
  }

}
