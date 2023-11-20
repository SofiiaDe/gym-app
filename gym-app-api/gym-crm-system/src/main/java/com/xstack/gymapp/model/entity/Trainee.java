package com.xstack.gymapp.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trainee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate birthDate;

  private String address;

  @ManyToMany
  @JoinTable(
      name = "trainee_trainer",
      joinColumns = @JoinColumn(name = "trainee_id"),
      inverseJoinColumns = @JoinColumn(name = "trainer_id")
  )
  private List<Trainer> trainers;

  @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Training> trainings;

  @Override
  public String toString() {
    return "Trainee{" +
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
        .map(Trainer::getId)
        .toList();
    return trainerIds.toString();
  }

  private String trainingsToString() {
    if (trainings == null) {
      return "null";
    }
    List<Long> trainingIds = trainings.stream()
        .map(Training::getId)
        .toList();
    return trainingIds.toString();
  }
}