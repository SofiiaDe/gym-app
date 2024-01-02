package com.xstack.gymapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xstack.gymapp.model.payload.ChangeUserActiveStatusRequest;
import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.RegistrationResponse;
import com.xstack.gymapp.model.payload.TraineeProfile;
import com.xstack.gymapp.model.payload.TraineeRegistrationRequest;
import com.xstack.gymapp.model.payload.TrainerShortInfo;
import com.xstack.gymapp.model.payload.UpdateTraineeProfileRequest;
import com.xstack.gymapp.model.payload.UpdateTraineeProfileResponse;
import com.xstack.gymapp.model.payload.UpdateTraineeTrainersListRequest;
import com.xstack.gymapp.service.TraineeService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    private static final String TRAINEES_API = "/api/trainees";

    private MockMvc mockMvc;

    @Mock
    private TraineeService traineeService;

    @InjectMocks
    private TraineeController traineeController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterTraineeWhenValidRequest() throws Exception {
        TraineeRegistrationRequest validRegistrationRequest = TraineeRegistrationRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .build();

        RegistrationResponse registrationResponse = RegistrationResponse.builder()
                .username("newUsername")
                .password("newPassword")
                .build();

        when(traineeService.createTrainee(any(TraineeRegistrationRequest.class))).thenReturn(
                registrationResponse);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(TRAINEES_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRegistrationRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("newUsername"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("newPassword"));
    }

    @Test
    void testRegisterTraineeWhenNullRequestProperties() throws Exception {
        TraineeRegistrationRequest invalidRegistrationRequest = TraineeRegistrationRequest.builder()
                .firstName(null)
                .lastName(null)
                .birthDate(null)
                .address(null)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(TRAINEES_API)
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
                });
    }

    @Test
    void testRegisterTraineeEmptyRequestProperties() throws Exception {
        TraineeRegistrationRequest invalidRegistrationRequest = TraineeRegistrationRequest.builder()
                .firstName("")
                .lastName("")
                .birthDate(null)
                .address("")
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(TRAINEES_API)
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
                });
    }

    @Test
    void testGetTraineeProfileWhenValidRequest() throws Exception {

        TraineeProfile traineeProfile = TraineeProfile.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .isActive(true)
                .build();

        when(traineeService.getTraineeProfile(any(String.class))).thenReturn(traineeProfile);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(TRAINEES_API + "/profile")
                .param("username", "validUsername")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("123 Main St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(true));
    }

    @Test
    void testGetTraineeProfileWhenNullRequestProperties() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(TRAINEES_API + "/profile")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testUpdateTraineeProfileWhenValidRequest() throws Exception {
        UpdateTraineeProfileRequest validUpdateRequest = UpdateTraineeProfileRequest.builder()
                .username("validUsername")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .isActive(true)
                .build();

        UpdateTraineeProfileResponse updatedProfileResponse = UpdateTraineeProfileResponse.builder()
                .username("validUsername")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .isActive(true)
                .build();

        when(traineeService.updateTrainee(any(UpdateTraineeProfileRequest.class))).thenReturn(
                updatedProfileResponse);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(TRAINEES_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUpdateRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("validUsername"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isActive").value(true));
    }

    @Test
    void testUpdateTraineeProfileNullRequestProperties() throws Exception {
        UpdateTraineeProfileRequest invalidUpdateRequest = UpdateTraineeProfileRequest.builder()
                .username(null)
                .firstName(null)
                .lastName(null)
                .birthDate(null)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(TRAINEES_API)
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
    }

    @Test
    void testUpdateTraineeProfileWhenEmptyRequestProperties() throws Exception {
        UpdateTraineeProfileRequest invalidUpdateRequest = UpdateTraineeProfileRequest.builder()
                .username("")
                .firstName("")
                .lastName("")
                .birthDate(null)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(TRAINEES_API)
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
    }

    @Test
    void testDeleteTraineeProfileWhenValidRequest() throws Exception {
        LoginRequest validLoginRequest = LoginRequest.builder()
                .username("validUsername")
                .password("validPassword")
                .build();
        String validUsername = "validUsername";

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(TRAINEES_API)
                .param("username", validUsername)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(traineeService).deleteTraineeByUsername(validUsername);
    }

    @Test
    void testDeleteTraineeProfileWhenInvalidRequest() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(TRAINEES_API)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(traineeService, never()).deleteTraineeByUsername(anyString());
    }

    @Test
    void testUpdateTraineeTrainersListWhenValidRequest() throws Exception {
        UpdateTraineeTrainersListRequest validUpdateRequest = UpdateTraineeTrainersListRequest.builder()
                .traineeUsername("validUsername")
                .trainerUsernames(Arrays.asList("trainer1", "trainer2"))
                .build();

        List<TrainerShortInfo> updatedTrainersList = Arrays.asList(
                TrainerShortInfo.builder().username("trainer1").build(),
                TrainerShortInfo.builder().username("trainer2").build()
        );

        when(traineeService.updateTraineeTrainersList(
                any(UpdateTraineeTrainersListRequest.class))).thenReturn(updatedTrainersList);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(TRAINEES_API + "/update-trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUpdateRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("trainer1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("trainer2"));
    }

    @Test
    void testUpdateTraineeTrainersListWhenInvalidRequest() throws Exception {
        UpdateTraineeTrainersListRequest invalidUpdateRequest = UpdateTraineeTrainersListRequest.builder()
                .traineeUsername(null)
                .trainerUsernames(null)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(TRAINEES_API + "/update-trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUpdateRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> {
                    String response = result.getResponse().getContentAsString();
                    Map<String, String> errors = objectMapper.readValue(response, new TypeReference<>() {
                    });
                    assertEquals("Trainee's username is required", errors.get("traineeUsername"));
                    assertEquals("List of trainers' usernames is required", errors.get("trainerUsernames"));
                });
    }

    @Test
    void testChangeTraineeActiveStatusWhenValidRequest() throws Exception {
        ChangeUserActiveStatusRequest validStatusChangeRequest = ChangeUserActiveStatusRequest.builder()
                .username("validUsername")
                .isActive(true)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch(TRAINEES_API + "/active-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validStatusChangeRequest));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Trainee activated successfully"));

        verify(traineeService).changeTraineeActiveStatus(validStatusChangeRequest);
    }

    @Test
    void testChangeTraineeActiveStatusWhenInvalidRequest() throws Exception {
        ChangeUserActiveStatusRequest invalidStatusChangeRequest = ChangeUserActiveStatusRequest.builder()
                .username(null)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch(TRAINEES_API + "/active-status")
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

        verify(traineeService, never()).changeTraineeActiveStatus(
                any(ChangeUserActiveStatusRequest.class));
    }
}
