package com.xstack.gymapp.model.mapper;

import com.xstack.gymapp.model.dto.TrainingTypeDto;
import com.xstack.gymapp.model.entity.TrainingType;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class TrainingTypeMapper implements EntityMapper<TrainingTypeDto, TrainingType> {

}

