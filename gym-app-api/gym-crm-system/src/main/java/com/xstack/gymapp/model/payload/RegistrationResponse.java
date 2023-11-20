package com.xstack.gymapp.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {

  @Schema(example = "John.Smith", description = "Unique username generated as a result concatenation of Trainer/Trainee first name and last name by using dot as a separator. In the case that already exists Trainer or Trainee with the same pair of first and last name as a suffix to the username is added serial number. ")
  private String username;

  @Schema(example = "ydtW4AVmpq", description = "Randomly generated password. ")
  private String password;
}
