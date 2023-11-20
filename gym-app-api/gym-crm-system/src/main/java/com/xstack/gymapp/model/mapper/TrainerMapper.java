package com.xstack.gymapp.model.mapper;

import com.xstack.gymapp.model.dto.TrainerDto;
import com.xstack.gymapp.model.entity.Trainer;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TrainingTypeMapper.class})
@Component
public abstract class TrainerMapper implements EntityMapper<TrainerDto, Trainer> {

}

