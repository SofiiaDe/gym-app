package com.xstack.gymapp.model.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.xstack.gymapp.model.enumeration.ActionType;
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
public class TrainerWorkloadRequest {

  @NotBlank(message = "Trainer Username is required")
  private String username;

  @NotBlank(message = "Trainer first name is required")
  private String firstName;

  @NotBlank(message = "Trainer last name is required")
  private String lastName;

  @JsonProperty("isActive")
  private boolean isActive;

  @NotNull(message = "Training date is required")
  @JsonFormat(pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate trainingDate;

  @NotNull(message = "Training duration is required")
  private Integer trainingDuration;

  @NotNull(message = "Action Type is required")
  private ActionType actionType;

}
