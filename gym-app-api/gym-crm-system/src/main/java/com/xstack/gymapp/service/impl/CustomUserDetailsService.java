package com.xstack.gymapp.service.impl;

import com.xstack.gymapp.exception.EntityNotFoundException;
import com.xstack.gymapp.model.entity.User;
import com.xstack.gymapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private static final String USER_NOT_FOUND_BY_USERNAME = "Cannot find the user with username = ";

  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND_BY_USERNAME + username));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getUsername())
        .password(user.getPassword())
        .roles()
        .build();
  }

}