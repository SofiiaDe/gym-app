package com.xstack.gymapp.trainermicroservice.model.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trainerTrainingSummaries")
@CompoundIndex(name = "firstName_lastName", def = "{'firstName': 1, 'lastName': 1}")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerTrainingSummary {

  @Id
  private String trainerId;
  private String firstName;
  private String lastName;

  @JsonProperty("isActive")
  private boolean isActive;
  private List<Year> years;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Year {

    private int year;
    private List<Month> months;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Month {

      private String monthName;
      private int trainingSummaryDuration;

    }
  }

}
