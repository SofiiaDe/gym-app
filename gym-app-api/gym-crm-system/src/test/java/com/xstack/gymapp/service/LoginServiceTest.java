package com.xstack.gymapp.service;

import com.xstack.gymapp.exception.EntityNotFoundException;
import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.LoginResponse;
import com.xstack.gymapp.model.payload.PasswordChangeRequest;
import com.xstack.gymapp.service.impl.LoginServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

  @Mock
  private UserService userService;

  @Mock
  private TokenService tokenService;

  @Mock
  private JwtService jwtService;

  @Mock
  private AuthenticationManager authenticationManager;

  @Mock
  private Authentication authentication;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private TrainerService trainerService;

  private LoginService loginService;
  private Validator validator;

  @BeforeEach
  public void setUp() {
    loginService = new LoginServiceImpl(userService, tokenService, jwtService,
        authenticationManager, passwordEncoder, trainerService);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testLoginWhenValidRequest() {
    String username = "testUser";
    String password = "testPassword";

    UserDto userDto = UserDto.builder()
        .username(username)
        .password(password)
        .build();

    LoginRequest loginRequest = LoginRequest.builder()
        .username(username)
        .password(password)
        .build();

    when(userService.getUserByUsername(username)).thenReturn(userDto);

    LoginResponse result = loginService.login(loginRequest);
    assertTrue(result.isLoggedIn());
  }

  @MockitoSettings(strictness = Strictness.LENIENT)
  @Test
  void testLoginWhenUserNotFound() {
    String username = "nonExistentUser";
    String password = "testPassword";

    LoginRequest loginRequest = LoginRequest.builder()
        .username(username)
        .password(password)
        .build();

    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenReturn(new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), loginRequest.getPassword()
        ));

    doThrow(EntityNotFoundException.class).when(
        userService).getUserByUsername(any(String.class));

    assertThrows(EntityNotFoundException.class,
        () -> loginService.login(loginRequest));

  }

  @Test
  void testLoginWhenWrongPassword() {
    String username = "testUser";
    String correctPassword = "correctPassword";
    String wrongPassword = "wrongPassword";

    UserDto userDto = UserDto.builder()
        .username(username)
        .password(correctPassword)
        .build();

    LoginRequest loginRequest = LoginRequest.builder()
        .username(username)
        .password(wrongPassword)
        .build();

    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));
  }

  @Test
  void testLoginWhenNullUsername() {
    String password = "testPassword";

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setPassword(password);

    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
    assertFalse(violations.isEmpty());
    assertEquals("Username is required", violations.iterator().next().getMessage());
  }

  @Test
  void testLoginWhenNullPassword() {
    String username = "testUser";

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setUsername(username);

    Set<ConstraintViolation<LoginRequest>> violations = validator.validate(loginRequest);
    assertFalse(violations.isEmpty());
    assertEquals("Password is required", violations.iterator().next().getMessage());
  }

  @Test
  void testChangeTraineePasswordWhenValidRequest() {
    PasswordChangeRequest request = PasswordChangeRequest.builder()
        .username("username")
        .oldPassword("oldPassword")
        .newPassword("newPassword10")
        .build();

    UserDto user = UserDto.builder()
        .username("username")
        .password("oldPassword")
        .build();

    when(userService.getUserByUsername(request.getUsername())).thenReturn(user);
    when(passwordEncoder.matches(request.getOldPassword(), user.getPassword())).thenReturn(true);
    when(passwordEncoder.encode(request.getNewPassword())).thenReturn("hashed_password");

    boolean result = loginService.changePassword(request);

    assertTrue(result);
    assertEquals("hashed_password", user.getPassword());
    verify(userService, times(1)).createUser(user);
  }

  @Test
  void testChangeTraineePasswordWhenInvalidCredentials() {
    PasswordChangeRequest request = PasswordChangeRequest.builder()
        .username("username")
        .oldPassword("oldPassword")
        .newPassword("newPassword10")
        .build();

    UserDto user = UserDto.builder()
        .username("username")
        .password("wrongPassword")
        .build();

    when(userService.getUserByUsername(request.getUsername())).thenReturn(user);

    boolean result = loginService.changePassword(request);

    assertFalse(result);
    assertEquals("wrongPassword", user.getPassword());
    verify(userService, never()).createUser(user);
  }

  @ParameterizedTest
  @MethodSource("validCredentialsProvider")
  void testChangePasswordWhenValidCredentials(PasswordChangeRequest request) {
    UserDto userDto = UserDto.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getOldPassword()))
        .build();

    when(userService.getUserByUsername(request.getUsername())).thenReturn(userDto);
    when(passwordEncoder.matches(request.getOldPassword(), userDto.getPassword())).thenReturn(true);

    assertTrue(loginService.changePassword(request));
  }

  @ParameterizedTest
  @MethodSource("invalidCredentialsProvider")
  void testChangePasswordWhenInvalidCredentials(UserDto user, PasswordChangeRequest request) {
    when(userService.getUserByUsername(request.getUsername())).thenReturn(user);
    assertFalse(loginService.changePassword(request));
  }

  private static Stream<PasswordChangeRequest> validCredentialsProvider() {
    return Stream.of(
        new PasswordChangeRequest("username", "oldPassword", "newPassword123"),
        new PasswordChangeRequest("anotherUser", "pass1234", "newPass567")
    );
  }

  private static Stream<Arguments> invalidCredentialsProvider() {
    return Stream.of(
        Arguments.of(createUser("username", "wrongPassword"),
            new PasswordChangeRequest("username", "oldPassword", "newPass1234")),
        Arguments.of(createUser("user", "pass1234"),
            new PasswordChangeRequest("user", "wrongOldPass", "newPass5678"))
    );
  }

  private static UserDto createUser(String username, String password) {
    return UserDto.builder()
        .username(username)
        .password(password)
        .build();
  }

}
