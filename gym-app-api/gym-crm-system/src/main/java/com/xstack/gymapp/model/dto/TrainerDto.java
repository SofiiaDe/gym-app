package com.xstack.gymapp.model.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDto {

  private Long id;
  private UserDto user;
  private TrainingTypeDto specialization;
  private List<TraineeDto> trainees;

}