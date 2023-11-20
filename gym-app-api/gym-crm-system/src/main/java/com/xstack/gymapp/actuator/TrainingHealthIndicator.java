package com.xstack.gymapp.actuator;

import com.xstack.gymapp.model.entity.Training;
import com.xstack.gymapp.repository.TrainingRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TrainingHealthIndicator implements HealthIndicator {

  @Autowired
  protected TrainingRepository trainingRepository;

  @Override
  public Health health() {
    int lowestCount = Integer.MAX_VALUE;

    Map<String, Integer> trainingDurations = new HashMap<>();

    for (Training training : trainingRepository.findTop3ByOrderByTrainingDurationDesc()) {
      trainingDurations.put(training.getTrainingName(), training.getTrainingDuration());

      if (training.getTrainingDuration() < lowestCount) {
        lowestCount = training.getTrainingDuration();
      }
    }

    if (lowestCount > 0) {
      return new Health.Builder().up().withDetail("trainings", trainingDurations).build();
    } else {
      return new Health.Builder().down().withDetail("trainings", trainingDurations).build();
    }
  }

}
