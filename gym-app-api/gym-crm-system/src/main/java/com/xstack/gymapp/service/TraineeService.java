package com.xstack.gymapp.service;

import com.xstack.gymapp.model.dto.TraineeDto;
import com.xstack.gymapp.model.dto.TrainerDto;
import com.xstack.gymapp.model.payload.ChangeUserActiveStatusRequest;
import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.RegistrationResponse;
import com.xstack.gymapp.model.payload.TraineeProfile;
import com.xstack.gymapp.model.payload.TraineeRegistrationRequest;
import com.xstack.gymapp.model.payload.TrainerShortInfo;
import com.xstack.gymapp.model.payload.UpdateTraineeProfileRequest;
import com.xstack.gymapp.model.payload.UpdateTraineeProfileResponse;
import com.xstack.gymapp.model.payload.UpdateTraineeTrainersListRequest;
import java.util.List;

public interface TraineeService {

  RegistrationResponse createTrainee(TraineeRegistrationRequest request);

  UpdateTraineeProfileResponse updateTrainee(UpdateTraineeProfileRequest updateRequest);

  void deleteTraineeById(Long traineeId);

  void deleteTraineeByUsername(String username);

  TraineeDto getTraineeById(Long traineeId);

  TraineeDto getTraineeByUsername(String username);

  void activateTrainee(Long traineeId);

  void deactivateTrainee(Long traineeId);

  void changeTraineeActiveStatus(ChangeUserActiveStatusRequest request);

  TraineeProfile  getTraineeProfile(String username);

  List<TrainerShortInfo> updateTraineeTrainersList(UpdateTraineeTrainersListRequest request);

}
