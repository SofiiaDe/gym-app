package com.xstack.gymapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.xstack.gymapp.exception.EntityNotFoundException;
import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.model.entity.User;
import com.xstack.gymapp.model.mapper.CycleAvoidingMappingContext;
import com.xstack.gymapp.model.mapper.UserMapper;
import com.xstack.gymapp.model.payload.PasswordChangeRequest;
import com.xstack.gymapp.repository.UserRepository;
import com.xstack.gymapp.service.impl.UserServiceImpl;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  private UserService userService;

  @BeforeEach
  public void setUp() {
    Random random = new Random();
    userService = new UserServiceImpl(userRepository, userMapper, random);
  }

  @Test
  void testGenerateUsernameWhenValidRequest() {
    String firstName = "John";
    String lastName = "Doe";

    when(userRepository.existsByUsername(anyString())).thenReturn(false);

    String generatedUsername = userService.generateUsername(firstName, lastName);

    verify(userRepository, times(1)).existsByUsername(anyString());
    assertNotNull(generatedUsername);
    assertTrue(generatedUsername.contains(firstName));
    assertTrue(generatedUsername.contains(lastName));
    assertEquals("John.Doe", generatedUsername);
  }

  @Test
  void testGeneratePassword() {
    String generatedPassword = userService.generatePassword();
    assertNotNull(generatedPassword);
    assertEquals(10, generatedPassword.length());
  }

  @Test
  void testCreateUserWhenValidRequest() {
    UserDto userDto = new UserDto();
    User userEntity = new User();

    when(userMapper.toEntity(any(UserDto.class), any(CycleAvoidingMappingContext.class))).thenReturn(userEntity);
    when(userRepository.save(any(User.class))).thenReturn(userEntity);
    when(userMapper.toDto(any(User.class), any(CycleAvoidingMappingContext.class))).thenReturn(userDto);

    UserDto createdUser = userService.createUser(userDto);

    verify(userMapper, times(1)).toEntity(any(UserDto.class), any(CycleAvoidingMappingContext.class));
    verify(userRepository, times(1)).save(any(User.class));
    verify(userMapper, times(1)).toDto(any(User.class), any(CycleAvoidingMappingContext.class));
    assertNotNull(createdUser);
    assertEquals(userDto, createdUser);
  }

  @Test
  void testAuthenticateUserWhenValidCredentials() {
    User user = new User();
    user.setUsername("john.smith");
    user.setPassword("securePassword");

    when(userRepository.findByUsername("john.smith")).thenReturn(Optional.of(user));

    boolean result = userService.authenticateUser("john.smith", "securePassword");

    assertTrue(result);
    verify(userRepository, times(1)).findByUsername("john.smith");
  }

  @Test
  void testAuthenticateUserWhenInvalidPassword() {
    User user = new User();
    user.setUsername("john.smith");
    user.setPassword("securePassword");

    when(userRepository.findByUsername("john.smith")).thenReturn(Optional.of(user));

    boolean result = userService.authenticateUser("john.smith", "incorrectPassword");

    assertFalse(result);
    verify(userRepository, times(1)).findByUsername("john.smith");
  }

  @Test
  void testAuthenticateUserWhenUserNotFound() {
    when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> userService.authenticateUser("unknownuser", "password"));
    verify(userRepository, times(1)).findByUsername("unknownuser");
  }


  @Test
  void testGetUserByUsernameWhenValidUsername() {
    User user = new User();
    user.setUsername("john.smith");

    UserDto userDto = new UserDto();
    userDto.setUsername("john.smith");

    when(userRepository.findByUsername("john.smith")).thenReturn(Optional.of(user));
    when(userMapper.toDto(any(User.class), any(CycleAvoidingMappingContext.class))).thenReturn(userDto);

    UserDto result = userService.getUserByUsername("john.smith");

    assertNotNull(result);
    assertEquals(userDto.getUsername(), result.getUsername());

    verify(userRepository, times(1)).findByUsername("john.smith");
    verify(userMapper, times(1)).toDto(eq(user), any(CycleAvoidingMappingContext.class));
  }

  @Test
  void testGetUserByUsernameWhenInvalidUsername() {
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> userService.getUserByUsername("nonexistentuser"));

    verify(userRepository, times(1)).findByUsername("nonexistentuser");
    verify(userMapper, never()).toDto(any(User.class), any(CycleAvoidingMappingContext.class));
  }

}