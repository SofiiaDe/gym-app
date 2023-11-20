package com.xstack.gymapp.trainermicroservice.service.impl;

import com.xstack.gymapp.trainermicroservice.exception.TrainerWorkloadProcessingException;
import com.xstack.gymapp.trainermicroservice.model.enumeration.ActionType;
import com.xstack.gymapp.trainermicroservice.model.payload.TrainerSummary;
import com.xstack.gymapp.trainermicroservice.model.payload.TrainerWorkloadRequest;
import com.xstack.gymapp.trainermicroservice.model.TrainersSummaryStorage;
import com.xstack.gymapp.trainermicroservice.service.MessageProducerService;
import com.xstack.gymapp.trainermicroservice.service.TrainerWorkloadService;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {

  private TrainersSummaryStorage trainersSummaryStorage;
  private MessageProducerService messageProducerService;

  public void updateTrainerWorkload(TrainerWorkloadRequest request) {
    try {
      TrainerSummary trainerSummary = getTrainerSummary(request);
      TrainerSummary updatedTrainerSummary = updateTrainerSummary(trainerSummary, request);
      trainersSummaryStorage.getStorage().put(trainerSummary.getUsername(), updatedTrainerSummary);
      messageProducerService.sendMessage(
          "The trainer's workload summary has been successfully updated.");
    } catch (Exception e) {
      throw new TrainerWorkloadProcessingException("Cannot update trainer's workload");
    }
  }

  private TrainerSummary getTrainerSummary(TrainerWorkloadRequest request) {
    TrainerSummary trainerSummary = trainersSummaryStorage.getStorage().get(request.getUsername());
    return trainerSummary != null ? trainerSummary :
        TrainerSummary.builder()
            .username(request.getUsername())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .isActive(request.isActive())
            .trainingSummaryDuration(new HashMap<>())
            .build();
  }

  private TrainerSummary updateTrainerSummary(TrainerSummary trainerSummary,
      TrainerWorkloadRequest request) {

    TrainerSummary updatedTrainerSummary = null;
    if (Objects.equals(request.getActionType(), ActionType.ADD)) {
      updatedTrainerSummary = addTraining(trainerSummary, request);
    } else if (Objects.equals(request.getActionType(), ActionType.DELETE)) {
      updatedTrainerSummary = deleteTraining(trainerSummary, request);
    }
    return updatedTrainerSummary;
  }

  private TrainerSummary addTraining(TrainerSummary trainerSummary,
      TrainerWorkloadRequest request) {
    LocalDate trainingDate = request.getTrainingDate();
    int year = trainingDate.getYear();
    int month = trainingDate.getMonthValue();

    Map<Integer, Map<String, Integer>> trainingSummaryDuration = trainerSummary.getTrainingSummaryDuration();

    Map<String, Integer> yearMap = trainingSummaryDuration.computeIfAbsent(year,
        k -> new HashMap<>());
    yearMap.put(getMonthName(month),
        yearMap.getOrDefault(getMonthName(month), 0) + request.getTrainingDuration());

    trainerSummary.setTrainingSummaryDuration(trainingSummaryDuration);
    return trainerSummary;
  }

  private String getMonthName(int month) {
    return Month.of(month).toString();
  }

  private TrainerSummary deleteTraining(TrainerSummary trainerSummary,
      TrainerWorkloadRequest request) {
    LocalDate trainingDate = request.getTrainingDate();

    // Extract the year and month from the training date
    int year = trainingDate.getYear();
    int month = trainingDate.getMonthValue();

    // Get the existing training summary duration map
    Map<Integer, Map<String, Integer>> trainingSummaryDuration = trainerSummary.getTrainingSummaryDuration();

    // Check if the year exists in the training summary
    if (trainingSummaryDuration.containsKey(year)) {
      Map<String, Integer> yearMap = trainingSummaryDuration.get(year);

      // Check if the month exists in the year's map
      if (yearMap != null && yearMap.containsKey(getMonthName(month))) {
        int existingDuration = yearMap.get(getMonthName(month));

        // Subtract the training duration from the existing month's duration
        existingDuration -= request.getTrainingDuration();

        // If the resulting duration is zero or negative, remove the month entry
        if (existingDuration <= 0) {
          yearMap.remove(getMonthName(month));
        } else {
          // Update the month's duration
          yearMap.put(getMonthName(month), existingDuration);
        }

        // If the year's map is empty, remove the year entry
        if (yearMap.isEmpty()) {
          trainingSummaryDuration.remove(year);
        }
      }
    }
    trainerSummary.setTrainingSummaryDuration(trainingSummaryDuration);
    return trainerSummary;
  }

}
