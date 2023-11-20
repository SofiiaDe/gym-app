package com.xstack.gymapp.service;

import com.xstack.gymapp.model.dto.TrainingDto;
import com.xstack.gymapp.model.payload.AddTrainingRequest;
import com.xstack.gymapp.model.payload.TraineeTrainingInfoDto;
import com.xstack.gymapp.model.payload.TrainerShortInfo;
import com.xstack.gymapp.model.payload.TrainerTrainingInfoDto;
import com.xstack.gymapp.model.payload.TrainerWorkloadRequest;
import com.xstack.gymapp.search.TraineeTrainingSearchCriteria;
import com.xstack.gymapp.search.TrainerTrainingSearchCriteria;
import java.util.List;

public interface TrainingService {

  void addTraining(AddTrainingRequest request);

  TrainingDto getTraining(Long trainingId);

  List<TraineeTrainingInfoDto> getTraineeTrainingsList(
      TraineeTrainingSearchCriteria searchCriteria);

  List<TrainerTrainingInfoDto> getTrainerTrainingsList(
      TrainerTrainingSearchCriteria searchCriteria);

  List<TrainerShortInfo> getUnassignedActiveTrainersForTrainee(String traineeUsername);

  void deleteTraining(Long trainingId);

  void updateTrainerWorkload(TrainerWorkloadRequest request);
}
