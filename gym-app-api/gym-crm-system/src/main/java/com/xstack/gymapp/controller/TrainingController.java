package com.xstack.gymapp.controller;

import com.xstack.gymapp.model.payload.AddTrainingRequest;
import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.search.TraineeTrainingSearchCriteria;
import com.xstack.gymapp.search.TrainerTrainingSearchCriteria;
import com.xstack.gymapp.service.TrainingService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/trainings")
@AllArgsConstructor
public class TrainingController extends BaseController {

  private TrainingService trainingService;
  private final MeterRegistry registry;

  @Timed(value = "add.training.time", description = "Time taken to add new training",
      percentiles = {0.5, 0.90})
  @Operation(summary = "Get trainee trainings list", description = "Get trainee trainings list")
  @PostMapping(value = "/trainee/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getTraineeTrainingsList(@Valid @RequestBody
  TraineeTrainingSearchCriteria searchCriteria, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return handleValidationErrors(bindingResult);
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(trainingService.getTraineeTrainingsList(searchCriteria));
  }

  @Operation(summary = "Get unassigned active trainers for trainee", description = "Get not assigned on trainee active trainers")
  @GetMapping(value = "/unassigned-trainers", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getUnassignedActiveTrainersForTrainee(@RequestParam("username") String username) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(trainingService.getUnassignedActiveTrainersForTrainee(username));
  }

  @Operation(summary = "Get trainer trainings list", description = "Get trainer trainings list")
  @PostMapping(value = "/trainer/list", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getTrainerTrainingsList(@Valid @RequestBody
  TrainerTrainingSearchCriteria searchCriteria, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return handleValidationErrors(bindingResult);
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(trainingService.getTrainerTrainingsList(searchCriteria));
  }

  @Timed(value = "add.training.time", description = "Time taken to add new training",
      percentiles = {0.5, 0.90})
  @Operation(summary = "Add training", description = "Add new training")
  @PostMapping("/add")
  public ResponseEntity<?> addTraining(@Valid @RequestBody AddTrainingRequest request,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return handleValidationErrors(bindingResult);
    }
    trainingService.addTraining(request);
    return ResponseEntity.status(HttpStatus.CREATED).body("Training added successfully");
  }

  @Operation(summary = "Delete training", description = "Delete training from the system")
  @DeleteMapping
  public ResponseEntity<?> deleteTraining(@RequestParam("id") Long trainingId) {
    trainingService.deleteTraining(trainingId);
    return ResponseEntity.status(HttpStatus.OK)
        .body("Training deleted successfully");
  }
}