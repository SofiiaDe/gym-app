package com.xstack.gymapp.model.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerShortInfo {

  private String username;
  private String firstName;
  private String lastName;
  private String specialization;

}
