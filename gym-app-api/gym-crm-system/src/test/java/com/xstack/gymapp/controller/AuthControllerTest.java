package com.xstack.gymapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xstack.gymapp.exception.EntityNotFoundException;
import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.LoginResponse;
import com.xstack.gymapp.model.payload.PasswordChangeRequest;
import com.xstack.gymapp.service.LoginService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  private MockMvc mockMvc;

  @Mock
  private LoginService loginService;

  @InjectMocks
  private AuthController authController;

  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void testLoginShouldLoginSuccessfully() throws Exception {
    LoginRequest validLoginRequest = LoginRequest.builder()
        .username("validUsername")
        .password("validPassword")
        .build();
    LoginResponse loginResponse = LoginResponse.builder()
        .isLoggedIn(true)
        .accessToken("accessToken")
        .refreshToken("refreshToken")
        .build();

    when(loginService.login(any(LoginRequest.class))).thenReturn(loginResponse);

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validLoginRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.isLoggedIn").value("true"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value("accessToken"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").value("refreshToken"));
  }

  @Test
  void testLoginWhenNullRequestProperties() throws Exception {
    LoginRequest invalidLoginRequest = LoginRequest.builder()
        .username(null)
        .password(null)
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidLoginRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result -> {
          String response = result.getResponse().getContentAsString();
          Map<String, String> errors = objectMapper.readValue(response,
              new TypeReference<>() {
              });
          assertEquals("Username is required", errors.get("username"));
          assertEquals("Password is required", errors.get("password"));
        });
  }

  @Test
  void testLoginWhenEmptyRequestProperties() throws Exception {
    LoginRequest invalidLoginRequest = LoginRequest.builder()
        .username("")
        .password("")
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidLoginRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result -> {
          String response = result.getResponse().getContentAsString();
          Map<String, String> errors = objectMapper.readValue(response,
              new TypeReference<>() {
              });
          assertEquals("Username is required", errors.get("username"));
          assertEquals("Password is required", errors.get("password"));
        });
  }

  @Test
  @Disabled
  void testLoginWhenFailedLogin() throws Exception {
    LoginRequest validLoginRequest = LoginRequest.builder()
        .username("User.Name")
        .password("password1234")
        .build();

    doThrow(EntityNotFoundException.class).when(
        loginService).login(any(LoginRequest.class));

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validLoginRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andExpect(MockMvcResultMatchers.content().string("Login failed"));
  }

  @Test
  void testChangePasswordShouldChangeSuccessfully() throws Exception {
    PasswordChangeRequest validChangePasswordRequest = PasswordChangeRequest.builder()
        .username("validUsername")
        .oldPassword("oldValidPassword")
        .newPassword("newValidPassword")
        .build();

    when(loginService.changePassword(any(PasswordChangeRequest.class))).thenReturn(true);

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validChangePasswordRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Password changed successfully"));
  }

  @Test
  void testChangePasswordWhenNullRequestProperties() throws Exception {
    PasswordChangeRequest invalidChangePasswordRequest = PasswordChangeRequest.builder()
        .username(null)
        .oldPassword(null)
        .newPassword(null)
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidChangePasswordRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result -> {
          String response = result.getResponse().getContentAsString();
          Map<String, String> errors = objectMapper.readValue(response, new TypeReference<>() {
          });
          assertEquals("Username is required", errors.get("username"));
          assertEquals("Old password is required", errors.get("oldPassword"));
          assertEquals("New password is required", errors.get("newPassword"));
        });
  }

  @Test
  @Disabled
  void testChangePasswordWhenEmptyRequestProperties() throws Exception {
    PasswordChangeRequest invalidChangePasswordRequest = PasswordChangeRequest.builder()
        .username("")
        .oldPassword("")
        .newPassword("")
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidChangePasswordRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result -> {
          String response = result.getResponse().getContentAsString();
          Map<String, String> errors = objectMapper.readValue(response, new TypeReference<>() {
          });
          assertEquals("Username is required", errors.get("username"));
          assertEquals("Old password is required", errors.get("oldPassword"));
          assertEquals("Password must be minimum 10 symbols", errors.get("newPassword"));
        });
  }

  @Test
  void testChangePasswordWhenShortNewPassword() throws Exception {
    PasswordChangeRequest invalidChangePasswordRequest = PasswordChangeRequest.builder()
        .username("User.Name")
        .oldPassword("password1234")
        .newPassword("short")
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidChangePasswordRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result -> {
          String response = result.getResponse().getContentAsString();
          Map<String, String> errors = objectMapper.readValue(response, new TypeReference<>() {
          });
          assertEquals("Password must be minimum 10 symbols", errors.get("newPassword"));
        });
  }

  @Test
  void testChangePasswordWhenFailedChangePassword() throws Exception {
    PasswordChangeRequest validChangePasswordRequest = PasswordChangeRequest.builder()
        .username("validUsername")
        .oldPassword("oldValidPassword")
        .newPassword("newValidPassword")
        .build();

    when(loginService.changePassword(any(PasswordChangeRequest.class))).thenReturn(false);

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .put("/api/auth/change-password")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validChangePasswordRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andExpect(MockMvcResultMatchers.content().string("Password change failed"));
  }
}