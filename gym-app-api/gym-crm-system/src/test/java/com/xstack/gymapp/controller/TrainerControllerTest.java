package com.xstack.gymapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xstack.gymapp.model.payload.ChangeUserActiveStatusRequest;
import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.RegistrationResponse;
import com.xstack.gymapp.model.payload.TraineeShortInfo;
import com.xstack.gymapp.model.payload.TrainerProfile;
import com.xstack.gymapp.model.payload.TrainerRegistrationRequest;
import com.xstack.gymapp.model.payload.UpdateTrainerProfileRequest;
import com.xstack.gymapp.model.payload.UpdateTrainerProfileResponse;
import com.xstack.gymapp.service.TrainerService;
import java.util.Arrays;
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
class TrainerControllerTest {

  private MockMvc mockMvc;

  @Mock
  private TrainerService trainerService;

  @InjectMocks
  private TrainerController trainerController;

  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void testRegisterTrainerWhenValidRequest() throws Exception {
    TrainerRegistrationRequest validRegistrationRequest = TrainerRegistrationRequest.builder()
        .firstName("John")
        .lastName("Doe")
        .specialization(123L)
        .build();

    RegistrationResponse registrationResponse = RegistrationResponse.builder()
        .username("newTrainerUsername")
        .password("newTrainerPassword")
        .build();

    when(trainerService.createTrainer(validRegistrationRequest)).thenReturn(registrationResponse);

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validRegistrationRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("newTrainerUsername"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("newTrainerPassword"));
  }

  @Test
  void testRegisterTrainerWhenInvalidRequest() throws Exception {
    TrainerRegistrationRequest invalidRegistrationRequest = TrainerRegistrationRequest.builder()
        .firstName(null)
        .lastName(null)
        .specialization(null)
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/trainer/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidRegistrationRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result -> {
          String response = result.getResponse().getContentAsString();
          Map<String, String> errors = objectMapper.readValue(response,
              new TypeReference<>() {
              });
          assertEquals("First Name is required", errors.get("firstName"));
          assertEquals("Last Name is required", errors.get("lastName"));
          assertEquals("Specialization is required", errors.get("specialization"));
        });
  }

  @Test
  void testGetTrainerProfileWhenValidRequest() throws Exception {
    LoginRequest validLoginRequest = LoginRequest.builder()
        .username("validUsername")
        .password("validPassword")
        .build();

    TrainerProfile trainerProfile = TrainerProfile.builder()
        .firstName("John")
        .lastName("Doe")
        .specialization("Fitness")
        .isActive(true)
        .trainees(Arrays.asList(
            TraineeShortInfo.builder().username("trainee1").build(),
            TraineeShortInfo.builder().username("trainee2").build()
        ))
        .build();

    when(trainerService.getTrainerProfile(validLoginRequest)).thenReturn(trainerProfile);

    mockMvc.perform(MockMvcRequestBuilders
            .get("/api/trainer/profile")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validLoginRequest)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.specialization").value("Fitness"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.trainees[0].username").value("trainee1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.trainees[1].username").value("trainee2"));
  }

  @Test
  void testGetTrainerProfileWhenInvalidRequest() throws Exception {
    LoginRequest invalidLoginRequest = LoginRequest.builder()
        .username(null)
        .password(null)
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/trainer/profile")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidLoginRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result -> {
          String response = result.getResponse().getContentAsString();
          Map<String, String> errors = objectMapper.readValue(response, new TypeReference<>() {
          });
          assertEquals("Username is required", errors.get("username"));
          assertEquals("Password is required", errors.get("password"));
        });

    verify(trainerService, never()).getTrainerProfile(any(LoginRequest.class));
  }

  @Test
  void testUpdateTrainerProfileWhenValidRequest() throws Exception {
    UpdateTrainerProfileRequest validUpdateRequest = UpdateTrainerProfileRequest.builder()
        .username("validUsername")
        .password("validPassword")
        .firstName("John")
        .lastName("Doe")
        .specialization("Fitness")
        .isActive(true)
        .build();

    UpdateTrainerProfileResponse updatedTrainerResponse = UpdateTrainerProfileResponse.builder()
        .username("validUsername")
        .firstName("John")
        .lastName("Doe")
        .specialization("Fitness")
        .isActive(true)
        .trainees(Arrays.asList(
            TraineeShortInfo.builder().username("trainee1").build(),
            TraineeShortInfo.builder().username("trainee2").build()
        ))
        .build();

    doReturn(updatedTrainerResponse).when(trainerService)
        .updateTrainer(any(UpdateTrainerProfileRequest.class));

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .put("/api/trainer/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validUpdateRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("validUsername"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.specialization").value("Fitness"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.trainees[0].username").value("trainee1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.trainees[1].username").value("trainee2"));
  }

  @Test
  void testUpdateTrainerProfileWhenInvalidRequest() throws Exception {
    UpdateTrainerProfileRequest invalidUpdateRequest = UpdateTrainerProfileRequest.builder()
        .username(null)
        .password(null)
        .firstName(null)
        .lastName(null)
        .specialization(null)
        .isActive(false)
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .put("/api/trainer/update")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidUpdateRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(result -> {
          String response = result.getResponse().getContentAsString();
          Map<String, String> errors = objectMapper.readValue(response,
              new TypeReference<>() {
              });
          assertEquals("Username is required", errors.get("username"));
          assertEquals("Password is required", errors.get("password"));
          assertEquals("First Name is required", errors.get("firstName"));
          assertEquals("Last Name is required", errors.get("lastName"));
        });

    verify(trainerService, never()).updateTrainer(any(UpdateTrainerProfileRequest.class));
  }

  @Test
  void testChangeTrainerActiveStatusWhenValidRequest() throws Exception {
    ChangeUserActiveStatusRequest validStatusChangeRequest = ChangeUserActiveStatusRequest.builder()
        .username("validUsername")
        .password("validPassword")
        .isActive(true)
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .patch("/api/trainer/active-status")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validStatusChangeRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("Trainer activated successfully"));

    verify(trainerService).changeTrainerActiveStatus(validStatusChangeRequest);
  }

  @Test
  @Disabled
  void testChangeTrainerActiveStatus_invalidRequest() throws Exception {
    ChangeUserActiveStatusRequest invalidStatusChangeRequest = ChangeUserActiveStatusRequest.builder()
        .username(null)
        .password(null)
        .isActive(false)
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .patch("/api/trainer/active-status")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidStatusChangeRequest));

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

    verify(trainerService, never()).changeTrainerActiveStatus(
        any(ChangeUserActiveStatusRequest.class));
  }

}