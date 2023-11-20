package com.xstack.gymapp.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.xstack.gymapp.model.entity.Token;
import com.xstack.gymapp.model.entity.Trainer;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String password;

  @JsonProperty("isActive")
  private boolean isActive;

  @JsonIgnore
  private List<Token> tokens;

  @Override
  public String toString() {
    return "UserDto{" +
        "id=" + id +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", isActive=" + isActive +
        ", tokens=" + tokensToString() +
        '}';
  }

  private String tokensToString() {
    if (tokens == null) {
      return "null";
    }
    List<Long> tokenIds = tokens.stream()
        .map(Token::getId)
        .toList();
    return tokenIds.toString();
  }
}