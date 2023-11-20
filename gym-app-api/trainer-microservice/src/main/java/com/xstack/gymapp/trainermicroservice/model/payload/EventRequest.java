package com.xstack.gymapp.trainermicroservice.model.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

  private String username;
  private String firstName;
  private String lastName;

  @JsonProperty("isActive")
  private boolean isActive;
  private int year;
  private String month;
  private int trainingDuration;

}
