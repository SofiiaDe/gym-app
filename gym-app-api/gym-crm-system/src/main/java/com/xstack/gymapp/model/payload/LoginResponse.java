package com.xstack.gymapp.model.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

  @JsonProperty("isLoggedIn")
  private boolean isLoggedIn;
  private String accessToken;
  private String refreshToken;
  private String userType;

}
