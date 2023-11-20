package com.xstack.gymapp.trainermicroservice.controller;

import com.xstack.gymapp.trainermicroservice.model.payload.TrainerWorkloadRequest;
import com.xstack.gymapp.trainermicroservice.service.TrainerWorkloadService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainer-workload")
@AllArgsConstructor
public class TrainerWorkloadController {

  private TrainerWorkloadService trainerWorkloadService;

  @PostMapping("/update")
  public ResponseEntity<String> updateTrainerWorkload(
      @Valid @RequestBody TrainerWorkloadRequest request) {
    trainerWorkloadService.updateTrainerWorkload(request);
    return ResponseEntity.status(HttpStatus.OK)
        .body("Workload updated successfully");
  }
}
