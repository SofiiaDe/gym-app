package com.xstack.gymapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class TrainerProcessingException extends RuntimeException {

  public TrainerProcessingException(String message) {
    super(message);
  }
}
