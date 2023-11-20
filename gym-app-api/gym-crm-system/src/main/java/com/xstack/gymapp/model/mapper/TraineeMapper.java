package com.xstack.gymapp.model.mapper;

import com.xstack.gymapp.model.dto.TraineeDto;
import com.xstack.gymapp.model.entity.Trainee;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TrainerMapper.class})
@Component
public abstract class TraineeMapper implements EntityMapper<TraineeDto, Trainee> {

}