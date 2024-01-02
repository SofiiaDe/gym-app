package com.xstack.gymapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.xstack.gymapp.exception.EntityNotFoundException;
import com.xstack.gymapp.exception.TraineeProcessingException;
import com.xstack.gymapp.model.dto.TraineeDto;
import com.xstack.gymapp.model.dto.TrainerDto;
import com.xstack.gymapp.model.dto.TrainingTypeDto;
import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.model.entity.Trainer;
import com.xstack.gymapp.model.entity.TrainingType;
import com.xstack.gymapp.model.entity.User;
import com.xstack.gymapp.model.mapper.CycleAvoidingMappingContext;
import com.xstack.gymapp.model.mapper.TrainerMapper;
import com.xstack.gymapp.model.mapper.UserMapper;
import com.xstack.gymapp.model.payload.RegistrationResponse;
import com.xstack.gymapp.model.payload.TrainerRegistrationRequest;
import com.xstack.gymapp.model.payload.UpdateTrainerProfileRequest;
import com.xstack.gymapp.model.payload.UpdateTrainerProfileResponse;
import com.xstack.gymapp.repository.TrainerRepository;
import com.xstack.gymapp.repository.UserRepository;
import com.xstack.gymapp.service.impl.TrainerServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

  @Mock
  private TrainerRepository trainerRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private TrainerMapper trainerMapper;

  @Mock
  private UserMapper userMapper;

  @Mock
  private UserService userService;

  @Mock
  private TrainingTypeService trainingTypeService;

  @Mock
  private PasswordEncoder passwordEncoder;

  private TrainerService trainerService;

  private Validator validator;

  @BeforeEach
  public void setUp() {
    trainerService = new TrainerServiceImpl(trainerRepository, userRepository, trainingTypeService,
        trainerMapper, userMapper, userService, passwordEncoder);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testCreateTrainerWhenValidRequest() {
    TrainerRegistrationRequest request = TrainerRegistrationRequest.builder()
        .firstName("John")
        .lastName("Doe")
        .specialization(1L)
        .build();

    String generatedUsername = "johndoe";
    String generatedPassword = "P@ssw0rd";
    TrainingTypeDto trainingTypeDto = new TrainingTypeDto();
    Trainer trainer = new Trainer();

    when(userService.generateUsername("John", "Doe")).thenReturn(generatedUsername);
    when(userService.generatePassword()).thenReturn(generatedPassword);

    when(trainingTypeService.getTrainingTypeById(1L)).thenReturn(trainingTypeDto);
    when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
    when(trainerMapper.toEntity(any(TrainerDto.class),
        any(CycleAvoidingMappingContext.class))).thenReturn(trainer);

    RegistrationResponse response = trainerService.createTrainer(request);

    assertNotNull(response);
    assertEquals(generatedUsername, response.getUsername());
    assertEquals(generatedPassword, response.getPassword());

    verify(userService).generateUsername("John", "Doe");
    verify(userService).generatePassword();
    verify(trainingTypeService).getTrainingTypeById(1L);
    verify(trainerRepository).save(any(Trainer.class));
  }

  @Test
  void testCreateTrainerWhenNullRequest() {
    assertThrows(NullPointerException.class, () -> trainerService.createTrainer(null));
  }

  @Test
  void testCreateTrainerWhenMissingFirstName() {
    TrainerRegistrationRequest request = TrainerRegistrationRequest.builder()
        .lastName("Doe")
        .specialization(1L)
        .build();

    Set<ConstraintViolation<TrainerRegistrationRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertEquals("First Name is required", violations.iterator().next().getMessage());
  }

  @Test
  void testCreateTrainerWhenMissingLastName() {
    TrainerRegistrationRequest request = TrainerRegistrationRequest.builder()
        .firstName("John")
        .specialization(1L)
        .build();

    Set<ConstraintViolation<TrainerRegistrationRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertEquals("Last Name is required", violations.iterator().next().getMessage());
  }

  @Test
  void testCreateTrainerWhenMissingSpecialization() {
    TrainerRegistrationRequest request = TrainerRegistrationRequest.builder()
        .firstName("John")
        .lastName("Doe")
        .build();

    Set<ConstraintViolation<TrainerRegistrationRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertEquals("Specialization is required", violations.iterator().next().getMessage());
  }

  @Test
  void testUpdateTrainerWhenValidRequest() {
    TrainerDto trainerDto = TrainerDto.builder()
        .id(1L)
        .trainees(List.of(
            TraineeDto.builder()
                .id(1L)
                .user(UserDto.builder()
                    .username("Trainee1")
                    .build())
                .birthDate(LocalDate.of(1991, 7, 23))
                .build(),
            TraineeDto.builder()
                .id(2L)
                .user(UserDto.builder()
                    .username("Trainer2")
                    .build())
                .birthDate(LocalDate.of(1993, 11, 7))
                .build()
        ))
        .build();
    UpdateTrainerProfileRequest updateRequest = UpdateTrainerProfileRequest.builder()
        .username("Test.Username")
        .specialization("Zumba")
        .build();
    User user = User.builder()
        .username("Test.Username")
        .build();
    Trainer trainer = Trainer.builder()
        .user(user)
        .specialization(TrainingType.builder()
            .trainingTypeName("Zumba")
            .build())
        .build();

    when(userRepository.findByUsername(updateRequest.getUsername()))
        .thenReturn((Optional.of(user)));
    when(trainerRepository.findByUser(any(User.class))).thenReturn(Optional.of(trainer));
    when(trainerMapper.toDto(any(Trainer.class), any(CycleAvoidingMappingContext.class)))
        .thenReturn(trainerDto);
    when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

    UpdateTrainerProfileResponse updatedTrainer = trainerService.updateTrainer(updateRequest);

    verify(trainerRepository, times(1)).save(any(Trainer.class));
    verify(trainerMapper, times(1)).toDto(any(Trainer.class),
        any(CycleAvoidingMappingContext.class));

    assertNotNull(updatedTrainer);
    assertEquals(updateRequest.getUsername(), updatedTrainer.getUsername());
    assertEquals(updateRequest.getSpecialization(), updatedTrainer.getSpecialization());
    assertEquals(2, updatedTrainer.getTrainees().size());
  }

  @Test
  void testUpdateTrainerWhenNonExistingTrainee() {
    UpdateTrainerProfileRequest updateRequest = UpdateTrainerProfileRequest.builder()
        .username("Test.Username")
        .build();
    User user = new User();

    when(userRepository.findByUsername(updateRequest.getUsername()))
        .thenReturn((Optional.of(user)));
    when(trainerRepository.findByUser(any(User.class))).thenReturn(Optional.empty());

    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> trainerService.updateTrainer(updateRequest));
    assertEquals("Cannot find the trainer with username = Test.Username", e.getMessage());
    verify(userRepository, times(1)).findByUsername(any(String.class));
    verify(trainerRepository, never()).save(any());
  }

  @Test
  void testUpdateTrainerWhenDatabaseError() {
    User user = User.builder()
        .username("Test.Username")
        .build();
    Trainer trainerToBeUpdated = Trainer.builder()
        .user(user)
        .build();

    UpdateTrainerProfileRequest updateRequest = UpdateTrainerProfileRequest.builder()
        .username("Test.Username")
        .build();

    when(userRepository.findByUsername(updateRequest.getUsername()))
        .thenReturn((Optional.of(user)));
    when(trainerRepository.findByUser(any(User.class))).thenReturn(Optional.of(trainerToBeUpdated));
    when(trainerRepository.save(trainerToBeUpdated)).thenThrow(
        new RuntimeException("Database error"));

    TraineeProcessingException e = assertThrows(TraineeProcessingException.class,
        () -> trainerService.updateTrainer(updateRequest));
    assertEquals("Cannot update the trainer with username = Test.Username", e.getMessage());
    verify(userRepository, times(1)).findByUsername(any(String.class));
    verify(trainerRepository, times(1)).findByUser(any(User.class));
    verify(trainerRepository, times(1)).save(trainerToBeUpdated);
  }

  @Test
  void testUpdateTrainerWhenEmptyResultDataAccessException() {
    UpdateTrainerProfileRequest updateRequest = UpdateTrainerProfileRequest.builder()
        .username("Test.Username")
        .build();
    User user = User.builder()
        .username("Test.Username")
        .build();
    Trainer trainerToBeUpdated = Trainer.builder()
        .user(user)
        .build();

    when(userRepository.findByUsername(updateRequest.getUsername()))
        .thenReturn((Optional.of(user)));
    when(trainerRepository.findByUser(any(User.class))).thenReturn(Optional.of(trainerToBeUpdated));
    when(trainerRepository.save(any())).thenThrow(new EmptyResultDataAccessException(1));

    TraineeProcessingException e = assertThrows(TraineeProcessingException.class,
        () -> trainerService.updateTrainer(updateRequest));
    assertEquals("Cannot update the trainer with username = Test.Username", e.getMessage());
    verify(userRepository, times(1)).findByUsername(any(String.class));
    verify(trainerRepository, times(1)).findByUser(any(User.class));
    verify(trainerRepository, times(1)).save(any());
  }

  @Test
  void testGetTrainerByIdWhenValidRequest() {
    TrainerDto trainerDto = new TrainerDto();
    Trainer trainerEntity = new Trainer();

    when(trainerRepository.findById(anyLong())).thenReturn(java.util.Optional.of(trainerEntity));
    when(
        trainerMapper.toDto(any(Trainer.class), any(CycleAvoidingMappingContext.class))).thenReturn(
        trainerDto);

    TrainerDto retrievedTrainer = trainerService.getTrainerById(1L);

    verify(trainerRepository, times(1)).findById(1L);
    verify(trainerMapper, times(1)).toDto(any(Trainer.class),
        any(CycleAvoidingMappingContext.class));

    assertNotNull(retrievedTrainer);
    assertEquals(trainerDto, retrievedTrainer);
  }

  @Test
  void testGetTrainerByUsernameWhenValidUsername() {
    User user = new User();
    user.setUsername("John.Smith");

    Trainer trainer = Trainer.builder()
        .id(1L)
        .user(user)
        .build();

    TrainerDto trainerDto = TrainerDto.builder()
        .id(1L)
        .user(UserDto.builder().username("John.Smith").build())
        .build();

    when(userRepository.findByUsername("John.Smith")).thenReturn(Optional.of(user));
    when(trainerRepository.findByUser(user)).thenReturn(Optional.of(trainer));
    when(
        trainerMapper.toDto(any(Trainer.class), any(CycleAvoidingMappingContext.class))).thenReturn(
        trainerDto);

    TrainerDto result = trainerService.getTrainerByUsername("John.Smith");

    assertNotNull(result);
    assertEquals(trainerDto.getId(), result.getId());
    assertEquals(trainerDto.getUser().getUsername(), result.getUser().getUsername());

    verify(userRepository, times(1)).findByUsername("John.Smith");
    verify(trainerRepository, times(1)).findByUser(user);
  }

  @Test
  void testGetTraineeByUsernameWhenInvalidUsername() {
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> trainerService.getTrainerByUsername("nonexistentuser"));

    verify(userRepository, times(1)).findByUsername("nonexistentuser");
    verify(trainerRepository, never()).findByUser(any(User.class));
    verify(trainerMapper, never()).toDto(any(Trainer.class),
        any(CycleAvoidingMappingContext.class));
  }

  @Test
  void testGetTraineeByUsernameWhenUserNotFound() {
    User user = new User();
    user.setUsername("John.Smith");

    when(userRepository.findByUsername("John.Smith")).thenReturn(Optional.of(user));
    when(trainerRepository.findByUser(user)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> trainerService.getTrainerByUsername("John.Smith"));

    verify(userRepository, times(1)).findByUsername("John.Smith");
    verify(trainerRepository, times(1)).findByUser(user);
    verify(trainerMapper, never()).toDto(any(Trainer.class),
        any(CycleAvoidingMappingContext.class));
  }

  @Test
  void testActivateTrainerWhenValidId() {
    Trainer trainer = new Trainer();
    trainer.setId(1L);
    User user = new User();
    trainer.setUser(user);

    when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));

    trainerService.activateTrainer(1L);

    assertTrue(trainer.getUser().isActive());
    verify(trainerRepository, times(1)).findById(1L);
    verify(trainerRepository, times(1)).save(trainer);
  }

  @Test
  void testDeactivateTrainerWhenValidId() {
    Trainer trainer = new Trainer();
    trainer.setId(1L);
    User user = new User();
    trainer.setUser(user);

    when(trainerRepository.findById(1L)).thenReturn(Optional.of(trainer));

    trainerService.deactivateTrainer(1L);

    assertFalse(trainer.getUser().isActive());
    verify(trainerRepository, times(1)).findById(1L);
    verify(trainerRepository, times(1)).save(trainer);
  }

  @Test
  void testActivateTrainerWhenInvalidId() {
    when(trainerRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> trainerService.activateTrainer(1L));

    verify(trainerRepository, times(1)).findById(1L);
    verify(trainerRepository, never()).save(any(Trainer.class));
  }

  @Test
  void testDeactivateTraineeWhenInvalidId() {
    when(trainerRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> trainerService.deactivateTrainer(1L));

    verify(trainerRepository, times(1)).findById(1L);
    verify(trainerRepository, never()).save(any(Trainer.class));
  }

  @Test
  void testGetAllTrainersByIsActiveWhenValidRequest() {
    User activeUser1 = new User();
    User activeUser2 = new User();
    when(userRepository.findAllByIsActive(true)).thenReturn(
        Arrays.asList(activeUser1, activeUser2));

    Trainer activeTrainer1 = new Trainer();
    activeTrainer1.setUser(activeUser1);
    Trainer activeTrainer2 = new Trainer();
    activeTrainer2.setUser(activeUser2);

    List<Trainer> activeTrainers = Arrays.asList(activeTrainer1, activeTrainer2);

    when(trainerRepository.findAllByUserIn(anyList())).thenReturn(activeTrainers);

    TrainerDto trainerDto1 = new TrainerDto();
    TrainerDto trainerDto2 = new TrainerDto();
    when(trainerMapper.toDto(any(Trainer.class), any(CycleAvoidingMappingContext.class))).thenReturn(
        trainerDto2);

    List<TrainerDto> result = trainerService.getAllTrainersByIsActive(true);

    assertEquals(2, result.size());
    assertEquals(trainerDto1, result.get(0));
    assertEquals(trainerDto2, result.get(1));

    verify(userRepository, times(1)).findAllByIsActive(true);
  }

}
