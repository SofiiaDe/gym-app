package com.xstack.gymapp.service;

import com.xstack.gymapp.model.dto.TrainerDto;
import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.model.payload.*;

import java.util.List;

public interface TrainerService {

  RegistrationResponse createTrainer(TrainerRegistrationRequest request);

  UpdateTrainerProfileResponse updateTrainer(UpdateTrainerProfileRequest request);

  TrainerDto getTrainerById(Long trainerId);

  TrainerDto getTrainerByUsername(String username);

  void activateTrainer(Long trainerId);

  void deactivateTrainer(Long trainerId);

  void changeTrainerActiveStatus(ChangeUserActiveStatusRequest request);

  List<TrainerDto> getAllTrainersByIsActive(boolean isActive);

  TrainerProfile getTrainerProfile(String username);

  boolean isTrainerByUserDto(UserDto userDto);

  List<TrainerShortInfo> getActiveTrainers(String traineeUsername);
}
