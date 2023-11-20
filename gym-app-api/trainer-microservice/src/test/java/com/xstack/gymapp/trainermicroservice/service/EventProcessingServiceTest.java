package com.xstack.gymapp.trainermicroservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.xstack.gymapp.trainermicroservice.model.payload.EventRequest;
import com.xstack.gymapp.trainermicroservice.model.payload.TrainerTrainingSummary;
import com.xstack.gymapp.trainermicroservice.repository.TrainerTrainingSummaryRepository;
import com.xstack.gymapp.trainermicroservice.service.impl.EventProcessingServiceImpl;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventProcessingServiceTest {

  @Mock
  private TrainerTrainingSummaryRepository trainerSummaryRepository;

  private EventProcessingService eventProcessingService;

  @BeforeEach
  public void setUp() {
    eventProcessingService = new EventProcessingServiceImpl(trainerSummaryRepository);
  }

  @Test
  void testProcessEventWhenTrainerExists() {
    TrainerTrainingSummary existingTrainer = TrainerTrainingSummary.builder()
        .firstName("John")
        .lastName("Doe")
        .years(new ArrayList<>())
        .build();

    when(trainerSummaryRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(
        existingTrainer);

    EventRequest eventRequest = EventRequest.builder()
        .firstName("John")
        .lastName("Doe")
        .year(2023)
        .month("September")
        .trainingDuration(120)
        .build();

    eventProcessingService.processEvent(eventRequest);

    assertEquals(1, existingTrainer.getYears().size());
    assertEquals(120,
        existingTrainer.getYears().get(0).getMonths().get(0).getTrainingSummaryDuration());
  }

  @Test
  void testProcessEventWhenTrainerDoesNotExist() {
    when(trainerSummaryRepository.findByFirstNameAndLastName("Alice", "Smith")).thenReturn(null);

    EventRequest eventRequest = EventRequest.builder()
        .firstName("Alice")
        .lastName("Smith")
        .year(2023)
        .month("September")
        .trainingDuration(180)
        .build();

    eventProcessingService.processEvent(eventRequest);

    verify(trainerSummaryRepository, times(1)).save(any(TrainerTrainingSummary.class));
  }

  @Test
  void testProcessEventWhenNoExistingYearRecord() {

    TrainerTrainingSummary existingTrainer = TrainerTrainingSummary.builder()
        .firstName("Mary")
        .lastName("Johnson")
        .years(new ArrayList<>())
        .build();

    when(trainerSummaryRepository.findByFirstNameAndLastName("Mary", "Johnson")).thenReturn(
        existingTrainer);

    EventRequest eventRequest = EventRequest.builder()
        .firstName("Mary")
        .lastName("Johnson")
        .year(2024)
        .month("January")
        .trainingDuration(60)
        .build();

    eventProcessingService.processEvent(eventRequest);

    assertEquals(1, existingTrainer.getYears().size());
    assertEquals(60,
        existingTrainer.getYears().get(0).getMonths().get(0).getTrainingSummaryDuration());
  }

  @Test
  void testProcessEventWhenNoExistingMonthRecord() {
    TrainerTrainingSummary existingTrainer = TrainerTrainingSummary.builder()
        .firstName("Bob")
        .lastName("Brown")
        .years(new ArrayList<>())
        .build();
    TrainerTrainingSummary.Year existingYear = TrainerTrainingSummary.Year.builder()
        .year(2023)
        .months(new ArrayList<>())
        .build();
    existingTrainer.getYears().add(existingYear);

    when(trainerSummaryRepository.findByFirstNameAndLastName("Bob", "Brown")).thenReturn(
        existingTrainer);

    EventRequest eventRequest = EventRequest.builder()
        .firstName("Bob")
        .lastName("Brown")
        .year(2023)
        .month("February")
        .trainingDuration(2)
        .build();

    eventProcessingService.processEvent(eventRequest);

    assertEquals(1, existingYear.getMonths().size());
    assertEquals(2, existingYear.getMonths().get(0).getTrainingSummaryDuration());
  }

}