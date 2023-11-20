package com.xstack.gymapp.repository;

import com.xstack.gymapp.model.entity.Training;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long>,
    JpaSpecificationExecutor<Training> {

  List<Training> findTop3ByOrderByTrainingDurationDesc();

}
