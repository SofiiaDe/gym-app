package com.xstack.gymapp.controller;

import com.xstack.gymapp.model.payload.ChangeUserActiveStatusRequest;
import com.xstack.gymapp.model.payload.LoginRequest;
import com.xstack.gymapp.model.payload.TraineeRegistrationRequest;
import com.xstack.gymapp.model.payload.TrainerShortInfo;
import com.xstack.gymapp.model.payload.UpdateTraineeProfileRequest;
import com.xstack.gymapp.model.payload.UpdateTraineeProfileResponse;
import com.xstack.gymapp.model.payload.UpdateTraineeTrainersListRequest;
import com.xstack.gymapp.service.TraineeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/api/trainees")
public class TraineeController extends BaseController {

    private TraineeService traineeService;

    @Operation(summary = "Register trainee", description = "Trainee registration")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerTrainee(@Valid @RequestBody TraineeRegistrationRequest request,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(traineeService.createTrainee(request));
    }

    @Operation(summary = "Get trainee profile", description = "Get trainee profile")
    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTraineeProfile(@RequestParam("username") String username) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(traineeService.getTraineeProfile(username));
    }

    @Operation(summary = "Update trainee profile", description = "Update trainee profile (all properties)")
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTraineeProfile(
            @Valid @RequestBody UpdateTraineeProfileRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        UpdateTraineeProfileResponse updatedTrainee = traineeService.updateTrainee(request);

        if (updatedTrainee == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedTrainee);
    }

    @Operation(summary = "Delete trainee profile", description = "Delete trainee profile")
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteTraineeProfile(@RequestParam("username") String username) {
        traineeService.deleteTraineeByUsername(username);
    }

    @Operation(summary = "Update Trainee's trainer list", description = "Update list of trainers assigned to trainee")
    @PutMapping(value = "/update-trainers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTraineeTrainersList(
            @Valid @RequestBody UpdateTraineeTrainersListRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        List<TrainerShortInfo> updatedTrainers = traineeService.updateTraineeTrainersList(request);

        if (updatedTrainers == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(updatedTrainers);
    }

    @Operation(summary = "Activate / deactivate trainee", description = "Change trainee's isActive status")
    @PatchMapping(value = "/active-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeTraineeActiveStatus(
            @Valid @RequestBody ChangeUserActiveStatusRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return handleValidationErrors(bindingResult);
        }

        traineeService.changeTraineeActiveStatus(request);
        String bodyMessage = request.isActive() ? "Trainee activated successfully" :
                "Trainee deactivated successfully";
        return ResponseEntity.status(HttpStatus.OK)
                .body(bodyMessage);
    }
}
