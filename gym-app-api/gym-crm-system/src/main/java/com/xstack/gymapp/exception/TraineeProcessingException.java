package com.xstack.gymapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TraineeProcessingException extends RuntimeException {

  public TraineeProcessingException(String message) {
    super(message);
  }
}
