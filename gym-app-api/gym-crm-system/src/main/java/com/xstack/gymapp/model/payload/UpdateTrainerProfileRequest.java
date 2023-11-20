package com.xstack.gymapp.model.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
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
public class UpdateTrainerProfileRequest {

  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "First Name is required")
  private String firstName;

  @NotBlank(message = "Last Name is required")
  private String lastName;

  @JsonProperty(access = Access.READ_ONLY)
  private String specialization;

  @NotNull(message = "Status is required")
  @JsonProperty("isActive")
  private boolean isActive;

}