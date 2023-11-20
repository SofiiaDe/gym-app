package com.xstack.gymapp.model.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRegistrationRequest {

  @NotNull(message = "First Name is required")
  private String firstName;

  @NotNull(message = "Last Name is required")
  private String lastName;

  @NotNull(message = "Specialization is required")
  private Long specialization;

}
