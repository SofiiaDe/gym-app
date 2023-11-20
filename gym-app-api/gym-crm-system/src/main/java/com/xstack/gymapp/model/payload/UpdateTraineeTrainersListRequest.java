package com.xstack.gymapp.model.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeTrainersListRequest {

  @NotBlank(message = "Trainee's username is required")
  private String traineeUsername;

  @NotNull(message = "List of trainers' usernames is required")
  private List<String> trainerUsernames;

}
