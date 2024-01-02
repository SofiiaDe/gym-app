package com.xstack.gymapp.service;

import com.xstack.gymapp.exception.EntityNotFoundException;
import com.xstack.gymapp.model.dto.*;
import com.xstack.gymapp.model.entity.*;
import com.xstack.gymapp.model.enumeration.ActionType;
import com.xstack.gymapp.model.enumeration.TrainingTypeName;
import com.xstack.gymapp.model.mapper.CycleAvoidingMappingContext;
import com.xstack.gymapp.model.mapper.TraineeMapper;
import com.xstack.gymapp.model.mapper.TrainerMapper;
import com.xstack.gymapp.model.mapper.TrainingMapper;
import com.xstack.gymapp.model.payload.*;
import com.xstack.gymapp.repository.*;
import com.xstack.gymapp.search.TraineeTrainingSearchCriteria;
import com.xstack.gymapp.search.TrainerTrainingSearchCriteria;
import com.xstack.gymapp.service.impl.TrainingServiceImpl;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    private static final String UPDATE_TRAINER_WORKLOAD_URL = "http://localhost:8080/api/trainer-workload/update";

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainingMapper trainingMapper;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TokenService tokenService;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private CompositeMeterRegistry meterRegistry;

    @Mock
    private Counter trainingCounter;
    @Mock
    private RestTemplate restTemplate;

    private TrainingService trainingService;

    @BeforeEach
    public void setUp() {
        AtomicInteger activeUsers = new AtomicInteger(0);
        when(meterRegistry.gauge(eq("number.of.active.users"), any(AtomicInteger.class))).thenReturn(
                activeUsers);
        when(meterRegistry.counter("add.training")).thenReturn(trainingCounter);

        trainingService = new TrainingServiceImpl(trainingRepository, trainingTypeRepository, userRepository,
                trainerRepository, traineeRepository, trainingMapper, traineeService, trainerService, tokenService,
                traineeMapper, trainerMapper, meterRegistry, restTemplate);
    }

    @Test
    @Disabled
    void testAddTrainingWhenValidRequest() {
        AddTrainingRequest request = createSampleRequest();
        TraineeDto traineeDto = new TraineeDto();
        TrainerDto trainerDto = new TrainerDto();
        TrainingType trainingType = new TrainingType();
        Training training = Training.builder()
                .trainer(Trainer.builder()
                        .user(User.builder().build())
                        .build())
                .build();
        String validToken = "valid.jwt.token";

        when(traineeService.getTraineeByUsername(request.getTraineeUsername())).thenReturn(
                traineeDto);
        when(trainerService.getTrainerByUsername(request.getTrainerUsername())).thenReturn(
                trainerDto);
        when(tokenService.getValidJWTTokenByUserId(any())).thenReturn(validToken);
        when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(
                request.getTrainingType())).thenReturn(trainingType);
        when(trainingRepository.save(any(Training.class))).thenReturn(training);
        doNothing().when(trainingCounter).increment();

        assertDoesNotThrow(() -> trainingService.addTraining(request));
    }

    @Test
    void testAddTrainingWhenInvalidTrainee() {
        AddTrainingRequest addTrainingRequest = createSampleRequest();

        doThrow(EntityNotFoundException.class).when(
                traineeService).getTraineeByUsername(addTrainingRequest.getTraineeUsername());

        assertThrows(EntityNotFoundException.class,
                () -> trainingService.addTraining(addTrainingRequest));
//        verify(traineeService, times(1)).getTraineeByUsername("Sample.Trainee");
        verify(trainingRepository, never()).save(any());
    }

    @Test
    void testAddTrainingWhenInvalidTrainer() {
        AddTrainingRequest addTrainingRequest = createSampleRequest();
        TraineeDto traineeDto = new TraineeDto();
        when(traineeService.getTraineeByUsername(addTrainingRequest.getTraineeUsername())).thenReturn(
                traineeDto);
        doThrow(EntityNotFoundException.class).when(
                trainerService).getTrainerByUsername(addTrainingRequest.getTrainerUsername());

        assertThrows(EntityNotFoundException.class,
                () -> trainingService.addTraining(addTrainingRequest));
//        verify(traineeService, times(1)).getTraineeByUsername("Sample.Trainee");
        verify(trainerService, times(1)).getTrainerByUsername("Sample.Trainer");
        verify(trainingRepository, never()).save(any());
    }

    @Test
    void testGetTrainingWhenValidRequest() {
        TrainingDto trainingDto = new TrainingDto();
        Training trainingEntity = new Training();

        when(trainingRepository.findById(anyLong())).thenReturn(
                java.util.Optional.of(trainingEntity));
        when(trainingMapper.toDto(any(Training.class),
                any(CycleAvoidingMappingContext.class))).thenReturn(trainingDto);

        TrainingDto retrievedTraining = trainingService.getTraining(1L);

        verify(trainingRepository, times(1)).findById(1L);
        verify(trainingMapper, times(1)).toDto(any(Training.class),
                any(CycleAvoidingMappingContext.class));

        assertNotNull(retrievedTraining);
        assertEquals(trainingDto, retrievedTraining);
    }

    @Test
    void testGetTrainingWhenTrainingNotFound() {
        when(trainingRepository.findById(anyLong())).thenReturn(
                Optional.empty());

        EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
                () -> trainingService.getTraining(1L));
        assertEquals("Cannot find the training with ID=1", e.getMessage());
        verify(trainingRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTraineeTrainingsListWhenValidRequest() {

        UserDto traineeUserDto = new UserDto();
        TraineeDto trainee = new TraineeDto();
        trainee.setUser(traineeUserDto);
        trainee.getUser().setUsername("traineeUsername");

        when(traineeService.getTraineeByUsername("traineeUsername")).thenReturn(trainee);

        TraineeTrainingSearchCriteria searchCriteria = new TraineeTrainingSearchCriteria();
        searchCriteria.setUsername("traineeUsername");

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Mock Training Type");

        Trainee mockTrainee = Trainee.builder()
                .user(User.builder()
                        .firstName("Mock Trainee")
                        .lastName("Surname")
                        .build())
                .build();

        Trainer mockTrainer = Trainer.builder()
                .user(User.builder()
                        .firstName("Mock Trainer")
                        .lastName("Surname")
                        .build())
                .build();

        Training training1 = Training.builder()
                .trainingName("Training 1")
                .trainingDate(LocalDate.now())
                .trainingType(trainingType)
                .trainingDuration(60)
                .trainee(mockTrainee)
                .trainer(mockTrainer)
                .build();

        Training training2 = Training.builder()
                .trainingName("Training 2")
                .trainingDate(LocalDate.now().plusDays(1))
                .trainingType(trainingType)
                .trainingDuration(90)
                .trainee(mockTrainee)
                .trainer(mockTrainer)
                .build();

        List<Training> mockTrainings = Arrays.asList(training1, training2);

        when(trainingRepository.findAll(any(Specification.class))).thenReturn(mockTrainings);

        List<TraineeTrainingInfoDto> result = trainingService.getTraineeTrainingsList(searchCriteria);

        assertEquals(2, result.size());

        TraineeTrainingInfoDto trainingInfo1 = result.get(0);
        assertEquals("Training 1", trainingInfo1.getTrainingName());
        assertEquals(LocalDate.now(), trainingInfo1.getTrainingDate());
        assertEquals("Mock Training Type", trainingInfo1.getTrainingType());
        assertEquals(60, trainingInfo1.getTrainingDuration());
        assertEquals("Mock Trainer Surname", trainingInfo1.getTrainerName());

        TraineeTrainingInfoDto trainingInfo2 = result.get(1);
        assertEquals("Training 2", trainingInfo2.getTrainingName());
        assertEquals(LocalDate.now().plusDays(1), trainingInfo2.getTrainingDate());
        assertEquals("Mock Training Type", trainingInfo2.getTrainingType());
        assertEquals(90, trainingInfo2.getTrainingDuration());
        assertEquals("Mock Trainer Surname", trainingInfo2.getTrainerName());

        verify(traineeService, times(1)).getTraineeByUsername("traineeUsername");
    }

    @Test
    void testGetTrainerTrainingsListWhenValidRequest() {

        TrainerDto trainer = new TrainerDto();
        UserDto trainerUserDto = new UserDto();
        trainer.setUser(trainerUserDto);
        trainer.getUser().setUsername("trainerUsername");

        when(trainerService.getTrainerByUsername("trainerUsername")).thenReturn(trainer);

        TrainerTrainingSearchCriteria searchCriteria = new TrainerTrainingSearchCriteria();
        searchCriteria.setUsername("trainerUsername");

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Mock Training Type");

        Trainer mockTrainer = Trainer.builder()
                .user(User.builder()
                        .firstName("Mock Trainee")
                        .lastName("Surname")
                        .build())
                .build();

        Trainee mockTrainee = Trainee.builder()
                .user(User.builder()
                        .firstName("Mock Trainee")
                        .lastName("Surname")
                        .build())
                .build();

        Training training1 = Training.builder()
                .trainingName("Training 1")
                .trainingDate(LocalDate.now())
                .trainingType(trainingType)
                .trainingDuration(60)
                .trainee(mockTrainee)
                .trainer(mockTrainer)
                .build();

        Training training2 = Training.builder()
                .trainingName("Training 2")
                .trainingDate(LocalDate.now().plusDays(1))
                .trainingType(trainingType)
                .trainingDuration(90)
                .trainee(mockTrainee)
                .trainer(mockTrainer)
                .build();

        List<Training> mockTrainings = Arrays.asList(training1, training2);

        when(trainingRepository.findAll(any(Specification.class))).thenReturn(mockTrainings);

        List<TrainerTrainingInfoDto> result = trainingService.getTrainerTrainingsList(searchCriteria);

        assertEquals(2, result.size());

        TrainerTrainingInfoDto trainingInfo1 = result.get(0);
        assertEquals("Training 1", trainingInfo1.getTrainingName());
        assertEquals(LocalDate.now(), trainingInfo1.getTrainingDate());
        assertEquals("Mock Training Type", trainingInfo1.getTrainingType());
        assertEquals(60, trainingInfo1.getTrainingDuration());
        assertEquals("Mock Trainee Surname", trainingInfo1.getTraineeName());

        TrainerTrainingInfoDto trainingInfo2 = result.get(1);
        assertEquals("Training 2", trainingInfo2.getTrainingName());
        assertEquals(LocalDate.now().plusDays(1), trainingInfo2.getTrainingDate());
        assertEquals("Mock Training Type", trainingInfo2.getTrainingType());
        assertEquals(90, trainingInfo2.getTrainingDuration());
        assertEquals("Mock Trainee Surname", trainingInfo2.getTraineeName());

        verify(trainerService, times(1)).getTrainerByUsername("trainerUsername");
    }

    @Test
    void testGetUnassignedActiveTrainersForTraineeWhenValidRequest() {
        TraineeDto trainee = new TraineeDto();
        UserDto userDto = new UserDto();
        userDto.setUsername("traineeUsername");
        trainee.setUser(userDto);
        trainee.setTrainers(Arrays.asList(
                TrainerDto.builder()
                        .id(123L)
                        .specialization(TrainingTypeDto.builder()
                                .trainingTypeName(TrainingTypeName.STRETCHING.getName())
                                .build())
                        .build(),
                TrainerDto.builder()
                        .id(456L)
                        .specialization(TrainingTypeDto.builder()
                                .trainingTypeName(TrainingTypeName.RESISTANCE.getName())
                                .build())
                        .build()));

        when(traineeService.getTraineeByUsername("traineeUsername")).thenReturn(trainee);

        TrainerDto activeTrainer1 = TrainerDto.builder()
                .user(new UserDto())
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName(TrainingTypeName.FITNESS.getName())
                        .build())
                .build();

        TrainerDto activeTrainer2 = TrainerDto.builder()
                .user(new UserDto())
                .specialization(TrainingTypeDto.builder()
                        .trainingTypeName(TrainingTypeName.YOGA.getName())
                        .build())
                .build();

        List<TrainerDto> allActiveTrainers = Arrays.asList(activeTrainer1, activeTrainer2);

        when(trainerService.getAllTrainersByIsActive(true)).thenReturn(allActiveTrainers);

        List<TrainerShortInfo> result = trainingService.getUnassignedActiveTrainersForTrainee(
                "traineeUsername");

        assertEquals(2, result.size());

        TrainerShortInfo trainerInfo1 = result.get(0);
        assertEquals(activeTrainer1.getUser().getUsername(), trainerInfo1.getUsername());
        assertEquals(activeTrainer1.getUser().getFirstName(), trainerInfo1.getFirstName());
        assertEquals(activeTrainer1.getUser().getLastName(), trainerInfo1.getLastName());
        assertEquals(activeTrainer1.getSpecialization().getTrainingTypeName(),
                trainerInfo1.getSpecialization());

        TrainerShortInfo trainerInfo2 = result.get(1);
        assertEquals(activeTrainer2.getUser().getUsername(), trainerInfo2.getUsername());
        assertEquals(activeTrainer2.getUser().getFirstName(), trainerInfo2.getFirstName());
        assertEquals(activeTrainer2.getUser().getLastName(), trainerInfo2.getLastName());
        assertEquals(activeTrainer2.getSpecialization().getTrainingTypeName(),
                trainerInfo2.getSpecialization());

        verify(traineeService, times(1)).getTraineeByUsername("traineeUsername");
        verify(trainerService, times(1)).getAllTrainersByIsActive(true);
    }

    @Test
//    @Disabled
    void testUpdateTrainerWorkloadWhenSuccessful() {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .username("testUser")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(60)
                .actionType(ActionType.ADD)
                .build();

        TrainerDto trainerDto = TrainerDto.builder()
                .user(UserDto.builder().build())
                .build();
        String validToken = "valid.jwt.token";
        when(trainerService.getTrainerByUsername(any(String.class))).thenReturn(trainerDto);
        when(tokenService.getValidJWTTokenByUserId(any())).thenReturn(validToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + validToken);
        HttpEntity<TrainerWorkloadRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> mockResponseEntity = new ResponseEntity<>("Success", HttpStatus.OK);

        when(restTemplate.exchange(
                eq(UPDATE_TRAINER_WORKLOAD_URL),
                eq(HttpMethod.POST),
                eq(requestEntity),
                eq(String.class)
        )).thenReturn(mockResponseEntity);

        trainingService.updateTrainerWorkload(request);

        verify(restTemplate, times(1)).exchange(
                eq(UPDATE_TRAINER_WORKLOAD_URL),
                eq(HttpMethod.POST),
                eq(requestEntity),
                eq(String.class)
        );
    }

    @Test
    @Disabled
    void testUpdateTrainerWorkload_HttpClientError() {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .username("testUser")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(60)
                .actionType(ActionType.DELETE)
                .build();

        TrainerDto trainerDto = TrainerDto.builder()
                .user(UserDto.builder().build())
                .build();
        String validToken = "valid.jwt.token";
        when(trainerService.getTrainerByUsername(any(String.class))).thenReturn(trainerDto);
        when(tokenService.getValidJWTTokenByUserId(any())).thenReturn(validToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + validToken);
        HttpEntity<TrainerWorkloadRequest> requestEntity = new HttpEntity<>(request, headers);

        when(restTemplate.exchange(
                eq(UPDATE_TRAINER_WORKLOAD_URL),
                eq(HttpMethod.POST),
                eq(requestEntity),
                eq(String.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            trainingService.updateTrainerWorkload(request);
        });

        verify(restTemplate, times(1)).exchange(
                eq(UPDATE_TRAINER_WORKLOAD_URL),
                eq(HttpMethod.POST),
                eq(requestEntity),
                eq(String.class)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Bad Request", exception.getStatusText());
    }

    @Test
//    @Disabled
    void testUpdateTrainerWorkloadWhenServerError() {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .username("testUser")
                .firstName("John")
                .lastName("Doe")
                .isActive(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(60)
                .actionType(ActionType.ADD)
                .build();

        TrainerDto trainerDto = TrainerDto.builder()
                .user(UserDto.builder().build())
                .build();
        String validToken = "valid.jwt.token";
        when(trainerService.getTrainerByUsername(any(String.class))).thenReturn(trainerDto);
        when(tokenService.getValidJWTTokenByUserId(any())).thenReturn(validToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + validToken);
        HttpEntity<TrainerWorkloadRequest> requestEntity = new HttpEntity<>(request, headers);

        when(restTemplate.exchange(
                eq(UPDATE_TRAINER_WORKLOAD_URL),
                eq(HttpMethod.POST),
                eq(requestEntity),
                eq(String.class)
        )).thenThrow(
                new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"));

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            trainingService.updateTrainerWorkload(request);
        });

        verify(restTemplate, times(1)).exchange(
                eq(UPDATE_TRAINER_WORKLOAD_URL),
                eq(HttpMethod.POST),
                eq(requestEntity),
                eq(String.class)
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Internal Server Error", exception.getStatusText());
    }

    private AddTrainingRequest createSampleRequest() {

        return AddTrainingRequest.builder()
                .traineeUsername("Sample.Trainee")
                .trainerUsername("Sample.Trainer")
                .trainingName("Sample Training")
                .trainingType("Sample Type")
                .trainingDate(LocalDate.now())
                .trainingDuration(5)
                .build();
    }
}
