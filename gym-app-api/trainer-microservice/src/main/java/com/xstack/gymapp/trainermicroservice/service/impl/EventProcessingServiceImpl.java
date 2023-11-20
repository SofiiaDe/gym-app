package com.xstack.gymapp.trainermicroservice.service.impl;

import com.xstack.gymapp.trainermicroservice.model.payload.EventRequest;
import com.xstack.gymapp.trainermicroservice.model.payload.TrainerTrainingSummary;
import com.xstack.gymapp.trainermicroservice.repository.TrainerTrainingSummaryRepository;
import com.xstack.gymapp.trainermicroservice.service.EventProcessingService;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class EventProcessingServiceImpl implements EventProcessingService {

  private TrainerTrainingSummaryRepository trainerTrainingSummaryRepository;

  @Override
  public void processEvent(EventRequest eventRequest) {
    String firstName = eventRequest.getFirstName();
    String lastName = eventRequest.getLastName();
    int year = eventRequest.getYear();
    String month = eventRequest.getMonth();
    int trainingDuration = eventRequest.getTrainingDuration();

    log.info("Processing event for Trainer: {} {}", firstName, lastName);

    TrainerTrainingSummary trainerRecord = trainerTrainingSummaryRepository.findByFirstNameAndLastName(
        firstName, lastName);

    if (trainerRecord == null) {
      log.info("Trainer not found. Creating a new record for {} {}.", firstName, lastName);
      trainerRecord = TrainerTrainingSummary.builder()
          .firstName(firstName)
          .lastName(lastName)
          .isActive(eventRequest.isActive())
          .years(new ArrayList<>())
          .build();
    }

    TrainerTrainingSummary.Year existingYear = null;

    for (TrainerTrainingSummary.Year yearEntry : trainerRecord.getYears()) {
      if (yearEntry.getYear() == year) {
        existingYear = yearEntry;
        break;
      }
    }

    if (existingYear == null) {
      log.info("No existing record for the year {}. Creating a new one.", year);
      existingYear = new TrainerTrainingSummary.Year();
      existingYear.setYear(year);
      existingYear.setMonths(new ArrayList<>());
      trainerRecord.getYears().add(existingYear);
    }

    TrainerTrainingSummary.Year.Month existingMonth = null;

    for (TrainerTrainingSummary.Year.Month monthEntry : existingYear.getMonths()) {
      if (monthEntry.getMonthName().equals(month)) {
        existingMonth = monthEntry;
        break;
      }
    }

    if (existingMonth == null) {
      log.info("No existing record for the month {}. Creating a new one.", month);
      existingMonth = new TrainerTrainingSummary.Year.Month();
      existingMonth.setMonthName(month);
      existingMonth.setTrainingSummaryDuration(0);
      existingYear.getMonths().add(existingMonth);
    }

    int previousDuration = existingMonth.getTrainingSummaryDuration();
    int newDuration = previousDuration + trainingDuration;
    existingMonth.setTrainingSummaryDuration(newDuration);

    log.info("Updating Trainings summary duration for {} {}: {} -> {}",
        firstName, lastName, previousDuration, newDuration);

    trainerTrainingSummaryRepository.save(trainerRecord);

    log.info("Event processing complete for {} {}.", firstName, lastName);
  }
}
