package com.xstack.gymapp.trainermicroservice.model.payload;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerSummary {

  private String username;
  private String firstName;
  private String lastName;
  private boolean isActive;
  private Map<Integer, Map<String, Integer>> trainingSummaryDuration;

}
