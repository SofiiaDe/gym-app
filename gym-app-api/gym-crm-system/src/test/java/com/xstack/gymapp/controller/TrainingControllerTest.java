package com.xstack.gymapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xstack.gymapp.model.payload.AddTrainingRequest;
import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.TraineeTrainingInfoDto;
import com.xstack.gymapp.model.payload.TrainerShortInfo;
import com.xstack.gymapp.model.payload.TrainerTrainingInfoDto;
import com.xstack.gymapp.search.TraineeTrainingSearchCriteria;
import com.xstack.gymapp.search.TrainerTrainingSearchCriteria;
import com.xstack.gymapp.service.TrainingService;
import java.time.LocalDate;
import java.util.ArrayList;
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
class TrainingControllerTest {

  private MockMvc mockMvc;

  @Mock
  private TrainingService trainingService;

  @InjectMocks
  private TrainingController trainingController;

  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void testGetTraineeTrainingsListWhenValidRequest() throws Exception {
    TraineeTrainingSearchCriteria validSearchCriteria = TraineeTrainingSearchCriteria.builder()
        .username("validUsername")
        .password("validPassword")
        .periodFrom(LocalDate.parse("2023-01-01"))
        .periodTo(LocalDate.parse("2023-12-31"))
        .trainerName("Trainer A")
        .trainingType("Cardio")
        .build();

    List<TraineeTrainingInfoDto> trainingsList = new ArrayList<>();
    TraineeTrainingInfoDto trainingInfoDto = TraineeTrainingInfoDto.builder()
        .trainingName("Training 1")
        .trainingDate(LocalDate.parse("2023-08-15"))
        .trainingType("Cardio")
        .trainingDuration(60)
        .trainerName("Trainer A")
        .build();
    trainingsList.add(trainingInfoDto);

    when(trainingService.getTraineeTrainingsList(validSearchCriteria)).thenReturn(trainingsList);

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/training/trainee/list")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validSearchCriteria));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].trainingName").value("Training 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].trainingDate").value("2023-08-15"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].trainingType").value("Cardio"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].trainingDuration").value(60))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].trainerName").value("Trainer A"));
  }

  @Test
  void testGetTraineeTrainingsListWhenInvalidRequest() throws Exception {
    TraineeTrainingSearchCriteria invalidSearchCriteria = TraineeTrainingSearchCriteria.builder()
        .username(null)
        .password(null)
        .periodFrom(null)
        .periodTo(null)
        .trainerName(null)
        .trainingType(null)
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/training/trainee/list")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidSearchCriteria));

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

    verify(trainingService, never()).getTraineeTrainingsList(
        any(TraineeTrainingSearchCriteria.class));
  }

  @Test
  void testGetUnassignedActiveTrainersForTraineeWhenValidRequest() throws Exception {
    LoginRequest validLoginRequest = LoginRequest.builder()
        .username("validUsername")
        .password("validPassword")
        .build();

    List<TrainerShortInfo> trainersList = new ArrayList<>();
    TrainerShortInfo trainerInfo = TrainerShortInfo.builder()
        .username("trainer1")
        .firstName("Trainer")
        .lastName("A")
        .specialization("Cardio")
        .build();
    trainersList.add(trainerInfo);

    when(trainingService.getUnassignedActiveTrainersForTrainee(
        validLoginRequest.getUsername())).thenReturn(trainersList);

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/training/unassigned-trainers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validLoginRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("trainer1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Trainer"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("A"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].specialization").value("Cardio"));
  }

  @Test
  void testGetUnassignedActiveTrainersForTraineeWhenInvalidRequest() throws Exception {
    LoginRequest invalidLoginRequest = LoginRequest.builder()
        .username(null)
        .password(null)
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/training/unassigned-trainers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidLoginRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    verify(trainingService, never()).getUnassignedActiveTrainersForTrainee(anyString());
  }

  @Test
  void testGetTrainerTrainingsListValidRequest() throws Exception {
    TrainerTrainingSearchCriteria validSearchCriteria = TrainerTrainingSearchCriteria.builder()
        .username("validUsername")
        .password("validPassword")
        .periodFrom(LocalDate.parse("2023-01-01"))
        .periodTo(LocalDate.parse("2023-12-31"))
        .traineeName("Trainee A")
        .build();

    List<TrainerTrainingInfoDto> trainingsList = new ArrayList<>();
    TrainerTrainingInfoDto trainingInfoDto = TrainerTrainingInfoDto.builder()
        .trainingName("Training 1")
        .trainingDate(LocalDate.parse("2023-08-15"))
        .trainingType("Cardio")
        .trainingDuration(60)
        .traineeName("Trainee A")
        .build();
    trainingsList.add(trainingInfoDto);

    when(trainingService.getTrainerTrainingsList(validSearchCriteria)).thenReturn(trainingsList);

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/training/trainer/list")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validSearchCriteria));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].trainingName").value("Training 1"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].trainingDate").value("2023-08-15"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].trainingType").value("Cardio"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].trainingDuration").value(60))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].traineeName").value("Trainee A"));
  }

  @Test
  void testGetTrainerTrainingsListWhenInvalidRequest() throws Exception {
    TrainerTrainingSearchCriteria invalidSearchCriteria = TrainerTrainingSearchCriteria.builder()
        .username(null)
        .password(null)
        .periodFrom(null)
        .periodTo(null)
        .traineeName(null)
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .get("/api/training/trainer/list")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidSearchCriteria)); // Provide valid JSON data

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    verify(trainingService, never()).getTrainerTrainingsList(
        any(TrainerTrainingSearchCriteria.class));
  }

  @Test
  void testAddTrainingWhenValidRequest() throws Exception {
    AddTrainingRequest validAddTrainingRequest = AddTrainingRequest.builder()
        .traineeUsername("TraineeA")
        .trainerUsername("TrainerB")
        .trainingName("Cardio Workout")
        .trainingType("Cardio")
        .trainingDate(LocalDate.parse("2023-08-20"))
        .trainingDuration(60)
        .build();

    doNothing().when(trainingService).addTraining(validAddTrainingRequest);

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/training/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(validAddTrainingRequest));

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().string("Training added successfully"));
  }

  @Test
  void testAddTrainingWhenInvalidRequest() throws Exception {
    AddTrainingRequest invalidAddTrainingRequest = AddTrainingRequest.builder()
        .traineeUsername(null)
        .trainerUsername(null)
        .trainingName(null)
        .trainingType(null)
        .trainingDate(null)
        .trainingDuration(null)
        .build();

    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
        .post("/api/training/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
            objectMapper.writeValueAsString(invalidAddTrainingRequest)); // Provide valid JSON data

    mockMvc.perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

    verify(trainingService, never()).addTraining(any(AddTrainingRequest.class));
  }
}

