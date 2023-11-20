package com.xstack.gymapp.controller;

import com.xstack.gymapp.model.payload.ChangeUserActiveStatusRequest;
import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.TrainerRegistrationRequest;
import com.xstack.gymapp.model.payload.UpdateTrainerProfileRequest;
import com.xstack.gymapp.model.payload.UpdateTrainerProfileResponse;
import com.xstack.gymapp.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api/trainers")
public class TrainerController extends BaseController {

  private TrainerService trainerService;

  @Operation(summary = "Register trainer", description = "Trainer registration")
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> registerTrainer(@Valid @RequestBody TrainerRegistrationRequest request,
      BindingResult result) {
    if (result.hasErrors()) {
      return handleValidationErrors(result);
    }
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(trainerService.createTrainer(request));
  }

  @Operation(summary = "Get trainer profile", description = "Get trainer profile")
  @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getTrainerProfile(@RequestParam("username") String username) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(trainerService.getTrainerProfile(username));
  }

  @Operation(summary = "Update trainer profile", description = "Update trainer profile (all properties)")
  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> updateTrainerProfile(
      @Valid @RequestBody UpdateTrainerProfileRequest request, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return handleValidationErrors(bindingResult);
    }

    UpdateTrainerProfileResponse updatedTrainer = trainerService.updateTrainer(request);

    if (updatedTrainer == null) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(updatedTrainer);
  }

  @Operation(summary = "Activate / deactivate trainer", description = "Change trainer's isActive status")
  @PatchMapping("/active-status")
  public ResponseEntity<String> changeTraineeActiveStatus(
      @Valid @RequestBody ChangeUserActiveStatusRequest request) {
    trainerService.changeTrainerActiveStatus(request);
    String bodyMessage = request.isActive() ? "Trainer activated successfully" :
        "Trainer deactivated successfully";
    return ResponseEntity.status(HttpStatus.OK)
        .body(bodyMessage);
  }

  @Operation(summary = "Get trainers", description = "Get trainers")
  @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> getActiveTrainers(@RequestParam("username") String traineeUsername) {
    return ResponseEntity.status(HttpStatus.OK)
            .body(trainerService.getActiveTrainers(traineeUsername));
  }
}
