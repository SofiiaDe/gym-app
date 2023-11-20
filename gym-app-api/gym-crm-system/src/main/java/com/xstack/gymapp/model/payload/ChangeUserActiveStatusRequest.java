package com.xstack.gymapp.model.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ChangeUserActiveStatusRequest {

  @NotBlank(message = "Username is required")
  private String username;

  @NotNull(message = "Status is required")
  @JsonProperty("isActive")
  private boolean isActive;

}
