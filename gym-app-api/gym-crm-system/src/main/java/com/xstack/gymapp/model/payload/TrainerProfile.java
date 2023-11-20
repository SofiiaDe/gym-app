package com.xstack.gymapp.model.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerProfile {

  private String firstName;
  private String lastName;
  private String specialization;

  @JsonProperty("isActive")
  private boolean isActive;
  private List<TraineeShortInfo> trainees;

}
