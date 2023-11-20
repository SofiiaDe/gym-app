package com.xstack.gymapp.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TraineeDto {

  private Long id;
  private UserDto user;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate birthDate;

  private String address;
  private List<TrainerDto> trainers;
  private List<TrainingDto> trainings;

  @Override
  public String toString() {
    return "TraineeDto{" +
        "id=" + id +
        ", user=" + (user != null ? user.getId() : null) +
        ", birthDate=" + birthDate +
        ", address='" + address + '\'' +
        ", trainers=" + trainersToString() +
        ", trainings=" + trainingsToString() +
        '}';
  }

  private String trainersToString() {
    if (trainers == null) {
      return "null";
    }
    List<Long> trainerIds = trainers.stream()
        .map(TrainerDto::getId)
        .toList();
    return trainerIds.toString();
  }

  private String trainingsToString() {
    if (trainings == null) {
      return "null";
    }
    List<Long> trainingIds = trainings.stream()
        .map(TrainingDto::getId)
        .toList();
    return trainingIds.toString();
  }

}
