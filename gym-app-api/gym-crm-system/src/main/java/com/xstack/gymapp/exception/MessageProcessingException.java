package com.xstack.gymapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MessageProcessingException extends RuntimeException {

  public MessageProcessingException(String message) {
    super(message);
  }

}
