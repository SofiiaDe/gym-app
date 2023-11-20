package com.xstack.gymapp.model.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddTrainingRequest {

  @NotBlank(message = "Trainee Username is required")
  private String traineeUsername;

  @NotBlank(message = "Trainer Username is required")
  private String trainerUsername;

  @NotBlank(message = "Training name is required")
  private String trainingName;

  @NotNull(message = "Training type is required")
  private String trainingType;

  @NotNull(message = "Training date is required")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate trainingDate;

  @NotNull(message = "Training duration is required")
  private Integer trainingDuration;
}
