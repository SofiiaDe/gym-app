package com.xstack.gymapp.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, name = "first_name")
  private String firstName;

  @Column(nullable = false, name = "last_name")
  private String lastName;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, name = "is_active")
  @JsonProperty("isActive")
  private boolean isActive;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Token> tokens;

  @Override
  public String toString() {
    return "User{" +
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