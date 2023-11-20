package com.xstack.gymapp.model.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

  @Schema(example = "John.Smith", description = "Unique username. ")
  @NotBlank(message = "Username is required")
  private String username;

  @Schema(example = "ydtW4AVmpq", description = "User password. ")
  @NotBlank(message = "Password is required")
  private String password;

}
