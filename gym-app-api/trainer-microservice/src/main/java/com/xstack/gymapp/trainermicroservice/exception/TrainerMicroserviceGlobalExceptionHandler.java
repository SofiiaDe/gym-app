package com.xstack.gymapp.trainermicroservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class TrainerMicroserviceGlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<String> handleAllExceptions(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }

  @ExceptionHandler(TrainerWorkloadProcessingException.class)
  public ResponseEntity<String> handleTrainerWorkloadProcessingException(TrainerWorkloadProcessingException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }
    
}
