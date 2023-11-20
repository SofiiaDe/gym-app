package com.xstack.gymapp.model.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeShortInfo {

  private String username;
  private String firstName;
  private String lastName;
  private boolean isActive;

}
