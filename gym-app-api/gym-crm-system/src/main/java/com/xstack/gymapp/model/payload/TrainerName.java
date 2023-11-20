package com.xstack.gymapp.model.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerName {

  private String trainerUsername;
  private String trainerFirstName;
  private String trainerLastName;

}
