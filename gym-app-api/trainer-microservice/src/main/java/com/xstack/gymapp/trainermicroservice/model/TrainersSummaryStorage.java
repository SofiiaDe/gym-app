package com.xstack.gymapp.trainermicroservice.model;

import com.xstack.gymapp.trainermicroservice.model.payload.TrainerSummary;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * The storage is in-memory saved structure containing trainer's monthly summary of the provided
 * trainings. Storage: key is trainer's username, value is trainings summary of this trainer
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class TrainersSummaryStorage {

  private Map<String, TrainerSummary> storage;
}
