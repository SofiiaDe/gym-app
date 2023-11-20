package com.xstack.gymapp.repository;

import com.xstack.gymapp.model.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {

  TrainingType findTrainingTypeByTrainingTypeName(String trainingTypeName);

}
