package com.xstack.gymapp.model.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {

  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "Old password is required")
  private String oldPassword;

  @Size(min = 10, message = "Password must be minimum 10 symbols")
  @NotBlank(message = "New password is required")
  private String newPassword;

}
