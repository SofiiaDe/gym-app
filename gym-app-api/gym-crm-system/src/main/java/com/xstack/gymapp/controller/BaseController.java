package com.xstack.gymapp.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

  protected ResponseEntity<?> handleValidationErrors(BindingResult result) {
    Map<String, String> errors = new HashMap<>();
    for (FieldError error : result.getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(errors);
  }

}
