package com.xstack.gymapp.trainermicroservice.repository;

import com.xstack.gymapp.trainermicroservice.model.payload.TrainerTrainingSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerTrainingSummaryRepository extends
    MongoRepository<TrainerTrainingSummary, String> {

  TrainerTrainingSummary findByFirstNameAndLastName(String firstName, String lastName);

  TrainerTrainingSummary save(TrainerTrainingSummary summary);
}
