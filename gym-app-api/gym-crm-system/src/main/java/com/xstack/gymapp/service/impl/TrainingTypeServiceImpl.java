package com.xstack.gymapp.service.impl;

import com.xstack.gymapp.exception.EntityNotFoundException;
import com.xstack.gymapp.model.dto.TrainingTypeDto;
import com.xstack.gymapp.model.entity.TrainingType;
import com.xstack.gymapp.model.mapper.CycleAvoidingMappingContext;
import com.xstack.gymapp.model.mapper.TrainingTypeMapper;
import com.xstack.gymapp.repository.TrainingTypeRepository;
import com.xstack.gymapp.service.TrainingTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

  private static final String TRAINING_TYPE_NOT_FOUND_BY_ID = "Cannot find the training type with ID=";

  private TrainingTypeRepository trainingTypeRepository;
  private TrainingTypeMapper trainingTypeMapper;

  @Override
  public TrainingTypeDto getTrainingTypeById(Long id) {
    TrainingType trainingType = trainingTypeRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(TRAINING_TYPE_NOT_FOUND_BY_ID + id));
    return trainingTypeMapper.toDto(trainingType, new CycleAvoidingMappingContext());
  }
}
