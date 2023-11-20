package com.xstack.gymapp.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import com.xstack.gymapp.model.entity.Trainee;
import com.xstack.gymapp.model.entity.Trainer;
import com.xstack.gymapp.model.entity.User;
import com.xstack.gymapp.model.enumeration.TrainingTypeName;
import com.xstack.gymapp.model.mapper.CycleAvoidingMappingContext;
import com.xstack.gymapp.model.mapper.TraineeMapper;
import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.RegistrationResponse;
import com.xstack.gymapp.model.payload.TraineeProfile;
import com.xstack.gymapp.model.payload.TraineeRegistrationRequest;
import com.xstack.gymapp.model.payload.TrainerShortInfo;
import com.xstack.gymapp.model.payload.UpdateTraineeProfileRequest;
import com.xstack.gymapp.model.payload.UpdateTraineeProfileResponse;
import com.xstack.gymapp.model.payload.UpdateTraineeTrainersListRequest;
import com.xstack.gymapp.repository.TraineeRepository;
import com.xstack.gymapp.repository.UserRepository;
import com.xstack.gymapp.service.impl.TraineeServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
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
class TraineeServiceTest {

  @Mock
  private TraineeRepository traineeRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private TraineeMapper traineeMapper;

  @Mock
  private UserService userService;

  @Mock
  private TrainerService trainerService;

  @Mock
  private PasswordEncoder passwordEncoder;

  private TraineeService traineeService;

  private Validator validator;


  @BeforeEach
  public void setUp() {
    traineeService = new TraineeServiceImpl(traineeRepository, userRepository, traineeMapper,
        userService, trainerService, passwordEncoder);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testCreateTraineeWhenValidRequest() {
    TraineeRegistrationRequest request = TraineeRegistrationRequest.builder()
        .firstName("John")
        .lastName("Doe")
        .address("123 Main St")
        .birthDate(LocalDate.of(1990, 1, 1))
        .build();

    String generatedUsername = "John.Doe";
    String generatedPassword = "P@ssw0rd";
    Trainee trainee = new Trainee();

    when(userService.generateUsername("John", "Doe")).thenReturn(generatedUsername);
    when(userService.generatePassword()).thenReturn(generatedPassword);
    when(traineeMapper.toEntity(any(TraineeDto.class),
        any(CycleAvoidingMappingContext.class))).thenReturn(trainee);
    when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

    RegistrationResponse response = traineeService.createTrainee(request);

    assertNotNull(response);
    assertEquals(generatedUsername, response.getUsername());
    assertEquals(generatedPassword, response.getPassword());

    verify(traineeRepository).save(any(Trainee.class));
  }

  @Test
  void testCreateTraineeWhenNullRequest() {
    assertThrows(NullPointerException.class, () -> traineeService.createTrainee(null));
  }

  @Test
  void testCreateTraineeWhenMissingFirstName() {
    TraineeRegistrationRequest request = TraineeRegistrationRequest.builder()
        .lastName("Doe")
        .address("123 Main St")
        .birthDate(LocalDate.of(1990, 1, 1))
        .build();

    Set<ConstraintViolation<TraineeRegistrationRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertEquals("First Name is required", violations.iterator().next().getMessage());
  }

  @Test
  void testCreateTraineeWhenMissingLastName() {
    TraineeRegistrationRequest request = TraineeRegistrationRequest.builder()
        .firstName("John")
        .address("123 Main St")
        .birthDate(LocalDate.of(1990, 1, 1))
        .build();

    Set<ConstraintViolation<TraineeRegistrationRequest>> violations = validator.validate(request);
    assertFalse(violations.isEmpty());
    assertEquals("Last Name is required", violations.iterator().next().getMessage());
  }

  @Test
  void testUpdateTraineeWhenValidRequest() {
    TraineeDto traineeDto = TraineeDto.builder()
        .id(1L)
        .trainers(List.of(
            TrainerDto.builder()
                .id(1L)
                .user(UserDto.builder()
                    .username("Trainer1")
                    .build())
                .specialization(TrainingTypeDto.builder()
                    .trainingTypeName(TrainingTypeName.FITNESS.getName())
                    .build())
                .build(),
            TrainerDto.builder()
                .id(2L)
                .user(UserDto.builder()
                    .username("Trainer2")
                    .build())
                .specialization(TrainingTypeDto.builder()
                    .trainingTypeName(TrainingTypeName.ZUMBA.getName())
                    .build())
                .build()
        ))
        .build();
    UpdateTraineeProfileRequest updateRequest = UpdateTraineeProfileRequest.builder()
        .username("Test.Username")
        .build();
    User user = User.builder()
        .username("Test.Username")
        .build();
    Trainee trainee = Trainee.builder()
        .user(user)
        .build();

    when(userRepository.findByUsername(updateRequest.getUsername()))
        .thenReturn((Optional.of(user)));
    when(traineeRepository.findByUser(any(User.class))).thenReturn(Optional.of(trainee));
    when(traineeMapper.toDto(any(Trainee.class), any(CycleAvoidingMappingContext.class)))
        .thenReturn(traineeDto);
    when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);

    UpdateTraineeProfileResponse updatedTrainee = traineeService.updateTrainee(updateRequest);

    verify(traineeRepository, times(1)).save(any(Trainee.class));
    verify(traineeMapper, times(1)).toDto(any(Trainee.class),
        any(CycleAvoidingMappingContext.class));

    assertNotNull(updatedTrainee);
    assertEquals(updateRequest.getUsername(), updatedTrainee.getUsername());
    assertEquals(2, updatedTrainee.getTrainers().size());
  }

  @Test
  void testUpdateTraineeWhenNonExistingTrainee() {
    UpdateTraineeProfileRequest updateRequest = UpdateTraineeProfileRequest.builder()
        .username("Test.Username")
        .build();
    User user = new User();

    when(userRepository.findByUsername(updateRequest.getUsername()))
        .thenReturn((Optional.of(user)));
    when(traineeRepository.findByUser(any(User.class))).thenReturn(Optional.empty());

    EntityNotFoundException e = assertThrows(EntityNotFoundException.class,
        () -> traineeService.updateTrainee(updateRequest));
    assertEquals("Cannot find the trainee with username = Test.Username", e.getMessage());
    verify(userRepository, times(1)).findByUsername(any(String.class));
    verify(traineeRepository, never()).save(any());
  }

  @Test
  void testUpdateTraineeWhenDatabaseError() {
    User user = User.builder()
        .username("Test.Username")
        .build();
    Trainee traineeToBeUpdated = Trainee.builder()
        .user(user)
        .build();

    UpdateTraineeProfileRequest updateRequest = UpdateTraineeProfileRequest.builder()
        .username("Test.Username")
        .build();

    when(userRepository.findByUsername(updateRequest.getUsername()))
        .thenReturn((Optional.of(user)));
    when(traineeRepository.findByUser(any(User.class))).thenReturn(Optional.of(traineeToBeUpdated));
    when(traineeRepository.save(traineeToBeUpdated)).thenThrow(
        new RuntimeException("Database error"));

    TraineeProcessingException e = assertThrows(TraineeProcessingException.class,
        () -> traineeService.updateTrainee(updateRequest));
    assertEquals("Cannot update the trainee with username = Test.Username", e.getMessage());
    verify(userRepository, times(1)).findByUsername(any(String.class));
    verify(traineeRepository, times(1)).findByUser(any(User.class));
    verify(traineeRepository, times(1)).save(traineeToBeUpdated);
  }

  @Test
  void testUpdateTraineeWhenEmptyResultDataAccessException() {
    UpdateTraineeProfileRequest updateRequest = UpdateTraineeProfileRequest.builder()
        .username("Test.Username")
        .build();
    User user = User.builder()
        .username("Test.Username")
        .build();
    Trainee traineeToBeUpdated = Trainee.builder()
        .user(user)
        .build();

    when(userRepository.findByUsername(updateRequest.getUsername()))
        .thenReturn((Optional.of(user)));
    when(traineeRepository.findByUser(any(User.class))).thenReturn(Optional.of(traineeToBeUpdated));
    when(traineeRepository.save(any())).thenThrow(new EmptyResultDataAccessException(1));

    TraineeProcessingException e = assertThrows(TraineeProcessingException.class,
        () -> traineeService.updateTrainee(updateRequest));
    assertEquals("Cannot update the trainee with username = Test.Username", e.getMessage());
    verify(userRepository, times(1)).findByUsername(any(String.class));
    verify(traineeRepository, times(1)).findByUser(any(User.class));
    verify(traineeRepository, times(1)).save(any());
  }

  @Test
  void testDeleteTraineeByIdWhenValidId() {
    Trainee trainee = new Trainee();
    trainee.setId(1L);

    when(traineeRepository.findById(1L)).thenReturn(Optional.of(trainee));

    assertDoesNotThrow(() -> traineeService.deleteTraineeById(1L));
    verify(traineeRepository, times(1)).findById(1L);
  }

  @Test
  void testDeleteTraineeByIdWhenInvalidId() {
    when(traineeRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> traineeService.deleteTraineeById(1L));

    verify(traineeRepository, times(1)).findById(1L);
    verify(traineeRepository, never()).deleteById(anyLong());
  }

  @Test
  void testGetTraineeWhenValidRequest() {
    TraineeDto traineeDto = new TraineeDto();
    Trainee traineeEntity = new Trainee();

    when(traineeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(traineeEntity));
    when(
        traineeMapper.toDto(any(Trainee.class), any(CycleAvoidingMappingContext.class))).thenReturn(
        traineeDto);

    TraineeDto retrievedTrainee = traineeService.getTraineeById(1L);

    verify(traineeRepository, times(1)).findById(1L);
    verify(traineeMapper, times(1)).toDto(any(Trainee.class),
        any(CycleAvoidingMappingContext.class));

    assertNotNull(retrievedTrainee);
    assertEquals(traineeDto, retrievedTrainee);
  }

  @Test
  void testGetTraineeByUsernameWhenValidUsername() {
    User user = new User();
    user.setUsername("John.Smith");

    Trainee trainee = new Trainee();
    trainee.setId(1L);
    trainee.setUser(user);

    TraineeDto traineeDto = new TraineeDto();
    traineeDto.setId(1L);
    traineeDto.setUser(UserDto.builder().username("John.Smith").build());
    traineeDto.getUser().setUsername("John.Smith");

    when(userRepository.findByUsername("John.Smith")).thenReturn(Optional.of(user));
    when(traineeRepository.findByUser(user)).thenReturn(Optional.of(trainee));
    when(traineeMapper.toDto(any(Trainee.class), any(CycleAvoidingMappingContext.class)))
        .thenReturn(traineeDto);

    TraineeDto result = traineeService.getTraineeByUsername("John.Smith");

    assertNotNull(result);
    assertEquals(traineeDto.getId(), result.getId());
    assertEquals(traineeDto.getUser().getUsername(), result.getUser().getUsername());

    verify(userRepository, times(1)).findByUsername("John.Smith");
    verify(traineeRepository, times(1)).findByUser(user);
    verify(traineeMapper, times(1)).toDto(any(Trainee.class),
        any(CycleAvoidingMappingContext.class));
  }

  @Test
  void testGetTraineeByUsernameWhenInvalidUsername() {
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> traineeService.getTraineeByUsername("nonexistentuser"));

    verify(userRepository, times(1)).findByUsername("nonexistentuser");
    verify(traineeRepository, never()).findByUser(any(User.class));
    verify(traineeMapper, never()).toDto(any(Trainee.class),
        any(CycleAvoidingMappingContext.class));
  }

  @Test
  void testGetTraineeByUsernameWhenUserNotFound() {
    User user = new User();
    user.setUsername("John.Smith");

    when(userRepository.findByUsername("John.Smith")).thenReturn(Optional.of(user));
    when(traineeRepository.findByUser(user)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> traineeService.getTraineeByUsername("John.Smith"));

    verify(userRepository, times(1)).findByUsername("John.Smith");
    verify(traineeRepository, times(1)).findByUser(user);
    verify(traineeMapper, never()).toDto(any(Trainee.class),
        any(CycleAvoidingMappingContext.class));
  }

  @Test
  void testActivateTraineeWhenValidId() {
    Trainee trainee = new Trainee();
    trainee.setId(1L);
    User user = new User();
    trainee.setUser(user);

    when(traineeRepository.findById(1L)).thenReturn(Optional.of(trainee));

    traineeService.activateTrainee(1L);

    assertTrue(trainee.getUser().isActive());
    verify(traineeRepository, times(1)).findById(1L);
    verify(traineeRepository, times(1)).save(trainee);
  }

  @Test
  void testDeactivateTraineeWhenValidId() {
    Trainee trainee = new Trainee();
    trainee.setId(1L);
    User user = new User();
    trainee.setUser(user);

    when(traineeRepository.findById(1L)).thenReturn(Optional.of(trainee));

    traineeService.deactivateTrainee(1L);

    assertFalse(trainee.getUser().isActive());
    verify(traineeRepository, times(1)).findById(1L);
    verify(traineeRepository, times(1)).save(trainee);
  }

  @Test
  void testActivateTraineeWhenInvalidId() {
    when(traineeRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> traineeService.activateTrainee(1L));

    verify(traineeRepository, times(1)).findById(1L);
    verify(traineeRepository, never()).save(any(Trainee.class));
  }

  @Test
  void testDeactivateTraineeWhenInvalidId() {
    when(traineeRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> traineeService.deactivateTrainee(1L));

    verify(traineeRepository, times(1)).findById(1L);
    verify(traineeRepository, never()).save(any(Trainee.class));
  }

  @Test
  void testDeleteTraineeByUsernameWhenValidUsername() {
    User user = new User();
    user.setUsername("john.smith");

    TraineeDto traineeDto = new TraineeDto();
    traineeDto.setUser(new UserDto());
    Trainee trainee = new Trainee();
    trainee.setUser(user);

    when(userRepository.findByUsername("john.smith")).thenReturn(Optional.of(user));
    when(traineeMapper.toDto(any(Trainee.class), any(CycleAvoidingMappingContext.class)))
        .thenReturn(traineeDto);
    when(traineeMapper.toEntity(any(TraineeDto.class),
        any(CycleAvoidingMappingContext.class))).thenReturn(trainee);
    when(traineeRepository.findByUser(user)).thenReturn(Optional.of(trainee));

    assertDoesNotThrow(() -> traineeService.deleteTraineeByUsername("john.smith"));

    verify(userRepository, times(1)).findByUsername("john.smith");
    verify(traineeRepository, times(1)).delete(any(Trainee.class));
    verify(traineeMapper, times(1)).toDto(any(Trainee.class),
        any(CycleAvoidingMappingContext.class));
  }

  @Test
  void testDeleteTraineeByUsernameWhenInvalidUsername() {
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> traineeService.deleteTraineeByUsername("nonexistentuser"));

    verify(userRepository, times(1)).findByUsername("nonexistentuser");
    verify(traineeRepository, never()).delete(any(Trainee.class));
    verify(traineeMapper, never()).toDto(any(Trainee.class),
        any(CycleAvoidingMappingContext.class));
  }

  @Test
  void testGetTraineeByUsernameWhenTraineeNotFound() {
    User user = new User();
    user.setUsername("john.smith");

    when(userRepository.findByUsername("john.smith")).thenReturn(Optional.of(user));
    when(traineeRepository.findByUser(user)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> traineeService.getTraineeByUsername("john.smith"));

    verify(userRepository, times(1)).findByUsername("john.smith");
    verify(traineeRepository, times(1)).findByUser(user);
    verify(traineeMapper, never()).toDto(any(Trainee.class),
        any(CycleAvoidingMappingContext.class));
  }

  @Test
  void testGetTraineeProfileWhenValidRequest() {
    LoginRequest loginRequest = new LoginRequest("username", "password");
    UserDto userDto = UserDto.builder()
        .username("username")
        .build();
    User user = User.builder()
        .username("username")
        .build();
    TraineeDto traineeDto = TraineeDto.builder()
        .user(userDto)
        .trainers(List.of(
            TrainerDto.builder()
                .id(1L)
                .user(UserDto.builder()
                    .username("Trainer1")
                    .build())
                .specialization(TrainingTypeDto.builder()
                    .trainingTypeName(TrainingTypeName.FITNESS.getName())
                    .build())
                .build(),
            TrainerDto.builder()
                .id(2L)
                .user(UserDto.builder()
                    .username("Trainer2")
                    .build())
                .specialization(TrainingTypeDto.builder()
                    .trainingTypeName(TrainingTypeName.ZUMBA.getName())
                    .build())
                .build()
        ))
        .build();
    Trainee trainee = Trainee.builder()
        .user(user)
        .trainers(List.of(
            Trainer.builder().id(1L).build(),
            Trainer.builder().id(2L).build()
        )).build();

    when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
    when(traineeRepository.findByUser(user)).thenReturn(Optional.of(trainee));
    when(traineeMapper.toDto(any(Trainee.class), any(CycleAvoidingMappingContext.class)))
        .thenReturn(traineeDto);
    TraineeProfile traineeProfile = traineeService.getTraineeProfile(loginRequest);

    assertNotNull(traineeProfile);
    assertEquals(traineeDto.getUser().getFirstName(), traineeProfile.getFirstName());
    assertEquals(traineeDto.getUser().getLastName(), traineeProfile.getLastName());
    assertEquals(traineeDto.getBirthDate(), traineeProfile.getBirthDate());
    assertEquals(traineeDto.getAddress(), traineeProfile.getAddress());
    assertEquals(traineeDto.getUser().isActive(), traineeProfile.isActive());

    assertNotNull(traineeProfile.getTrainers());
    assertEquals(traineeDto.getTrainers().size(), traineeProfile.getTrainers().size());
    for (int i = 0; i < traineeDto.getTrainers().size(); i++) {
      assertEquals(traineeDto.getTrainers().get(i).getUser().getUsername(),
          traineeProfile.getTrainers().get(i).getUsername());
      assertEquals(traineeDto.getTrainers().get(i).getUser().getFirstName(),
          traineeProfile.getTrainers().get(i).getFirstName());
      assertEquals(traineeDto.getTrainers().get(i).getUser().getLastName(),
          traineeProfile.getTrainers().get(i).getLastName());
      assertEquals(traineeDto.getTrainers().get(i).getSpecialization().getTrainingTypeName(),
          traineeProfile.getTrainers().get(i).getSpecialization());
    }
    verify(userRepository, times(1)).findByUsername(any(String.class));
    verify(traineeRepository, times(1)).findByUser(any(User.class));
  }

  @Test
  void testGetTraineeProfileWhenNotFoundUser() {
    LoginRequest loginRequest = new LoginRequest("nonExistentUsername", "password");

    Exception exception = assertThrows(EntityNotFoundException.class, () -> {
      traineeService.getTraineeProfile(loginRequest);
    });

    assertEquals("Cannot find the user with username = " + loginRequest.getUsername(),
        exception.getMessage());
  }

  @Test
  void testGetTraineeProfileWhenNullTrainers() {
    LoginRequest loginRequest = new LoginRequest("usernameWithNoTrainers", "password");
    UserDto userDto = UserDto.builder()
        .username("usernameWithNoTrainers")
        .build();
    User user = User.builder()
        .username("usernameWithNoTrainers")
        .build();
    TraineeDto traineeDto = TraineeDto.builder()
        .user(userDto)
        .trainers(null)
        .build();
    traineeDto.setTrainers(null);
    Trainee trainee = Trainee.builder()
        .user(user)
        .trainers(null)
        .build();

    when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(Optional.of(user));
    when(traineeRepository.findByUser(user)).thenReturn(Optional.of(trainee));
    when(traineeMapper.toDto(any(Trainee.class), any(CycleAvoidingMappingContext.class)))
        .thenReturn(traineeDto);

    TraineeProfile traineeProfile = traineeService.getTraineeProfile(loginRequest);

    assertNotNull(traineeProfile);
    assertNotNull(traineeProfile.getTrainers());
    assertEquals(0, traineeProfile.getTrainers().size());
  }

  @Test
  void testUpdateTraineeTrainersListWhenValidRequest() {
    UpdateTraineeTrainersListRequest updateRequest = UpdateTraineeTrainersListRequest.builder()
        .traineeUsername("Test.Username")
        .trainerUsernames(List.of("trainerUsername1", "trainerUsername2"))
        .build();
    User user = User.builder()
        .username("Test.Username")
        .build();
    Trainee trainee = Trainee.builder()
        .user(user)
        .build();

    TrainerDto trainerDto1 = TrainerDto.builder()
        .user(UserDto.builder()
            .username("trainerUsername1")
            .build())
        .specialization(TrainingTypeDto.builder()
            .trainingTypeName("Resistance")
            .build())
        .build();
    TrainerDto trainerDto2 = TrainerDto.builder()
        .user(UserDto.builder()
            .username("trainerUsername2")
            .build())
        .specialization(TrainingTypeDto.builder()
            .trainingTypeName("Yoga")
            .build())
        .build();

    TraineeDto updatedTraineeDto = TraineeDto.builder()
        .trainers(List.of(trainerDto1, trainerDto2))
        .build();

    Trainer trainer1 = Trainer.builder()
        .user(User.builder()
            .username("trainerUsername1")
            .build())
        .build();
    Trainer trainer2 = Trainer.builder()
        .user(User.builder()
            .username("trainerUsername2")
            .build())
        .build();

    Trainee updatedTrainee = Trainee.builder()
        .trainers(List.of(trainer1, trainer2))
        .build();

    when(userRepository.findByUsername(updateRequest.getTraineeUsername()))
        .thenReturn((Optional.of(user)));
    when(traineeRepository.findByUser(any(User.class))).thenReturn(Optional.of(trainee));
    when(trainerService.getTrainerByUsername("trainerUsername1")).thenReturn(trainerDto1);
    when(trainerService.getTrainerByUsername("trainerUsername2")).thenReturn(trainerDto2);
    when(traineeMapper.toEntity(any(TraineeDto.class), any(CycleAvoidingMappingContext.class)))
        .thenReturn(updatedTrainee);
    when(traineeRepository.save(any(Trainee.class))).thenReturn(updatedTrainee);
    when((traineeMapper.toDto(any(Trainee.class), any(CycleAvoidingMappingContext.class))))
        .thenReturn(updatedTraineeDto);

    List<TrainerShortInfo> result = traineeService.updateTraineeTrainersList(updateRequest);

    assertEquals(2, result.size());
    assertEquals("trainerUsername1", result.get(0).getUsername());
    assertEquals("trainerUsername2", result.get(1).getUsername());
    assertEquals("Resistance", result.get(0).getSpecialization());
    assertEquals("Yoga", result.get(1).getSpecialization());

    verify(userRepository, times(1)).findByUsername("Test.Username");
    verify(traineeRepository, times(1)).findByUser(user);
    verify(trainerService, times(1)).getTrainerByUsername("trainerUsername1");
    verify(trainerService, times(1)).getTrainerByUsername("trainerUsername2");
    verify(traineeRepository, times(1)).save(updatedTrainee);
  }
}