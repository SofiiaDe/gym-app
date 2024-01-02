package com.xstack.gymapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xstack.gymapp.model.payload.*;
import com.xstack.gymapp.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {

    private static final String TRAINERS_API = "/api/trainers";

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
                .post(TRAINERS_API)
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
                .post(TRAINERS_API)
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

        String userName = "validUsername";

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

        when(trainerService.getTrainerProfile(userName)).thenReturn(trainerProfile);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(TRAINERS_API + "/profile")
                        .param("username", userName)
                        .contentType(MediaType.APPLICATION_JSON))
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

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(TRAINERS_API + "/profile")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(trainerService, never()).getTrainerProfile(any(String.class));
    }

    @Test
    void testUpdateTrainerProfileWhenValidRequest() throws Exception {
        UpdateTrainerProfileRequest validUpdateRequest = UpdateTrainerProfileRequest.builder()
                .username("validUsername")
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
                .put(TRAINERS_API)
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
                .firstName(null)
                .lastName(null)
                .specialization(null)
                .isActive(false)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(TRAINERS_API)
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
                    assertEquals("First Name is required", errors.get("firstName"));
                    assertEquals("Last Name is required", errors.get("lastName"));
                });

        verify(trainerService, never()).updateTrainer(any(UpdateTrainerProfileRequest.class));
    }

    @Test
    void testChangeTrainerActiveStatusWhenValidRequest() throws Exception {
        ChangeUserActiveStatusRequest validStatusChangeRequest = ChangeUserActiveStatusRequest.builder()
                .username("validUsername")
                .isActive(true)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch(TRAINERS_API + "/active-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validStatusChangeRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Trainer activated successfully"));

        verify(trainerService).changeTrainerActiveStatus(validStatusChangeRequest);
    }

    @Test
    void testChangeTrainerActiveStatusWhenInvalidRequest() throws Exception {
        ChangeUserActiveStatusRequest invalidStatusChangeRequest = ChangeUserActiveStatusRequest.builder()
                .username(null)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch(TRAINERS_API + "/active-status")
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
                });

        verify(trainerService, never()).changeTrainerActiveStatus(
                any(ChangeUserActiveStatusRequest.class));
    }

}