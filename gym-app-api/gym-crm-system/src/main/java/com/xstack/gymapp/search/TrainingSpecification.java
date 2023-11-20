package com.xstack.gymapp.search;

import com.xstack.gymapp.model.entity.Trainee;
import com.xstack.gymapp.model.entity.Training;
import java.time.LocalDate;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

public class TrainingSpecification {

  private TrainingSpecification() {
  }

  public static Specification<Training> trainee(Trainee trainee) {
    return (root, query, cb) -> cb.equal(root.get("trainee"), trainee);
  }

  public static Specification<Training> periodFrom(LocalDate date) {
    return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("trainingDate"), date);
  }

  public static Specification<Training> periodTo(LocalDate date) {
    return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("trainingDate"), date);
  }

  public static Specification<Training> trainerName(String name) {
    return (root, query, cb) -> cb.like(root.get("trainer").get("user").get("fullName"),
        "%" + name + "%");
  }

  public static Specification<Training> trainingType(String type) {
    return (root, query, cb) -> cb.equal(root.get("trainingType").get("name"), type);
  }

}
