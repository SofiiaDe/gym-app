package com.xstack.gymapp.model.mapper;

import com.xstack.gymapp.model.dto.TrainingDto;
import com.xstack.gymapp.model.entity.Training;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class TrainingMapper implements EntityMapper<TrainingDto, Training> {

}

