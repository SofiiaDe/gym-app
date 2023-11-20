package com.xstack.gymapp.model.mapper;

import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.model.entity.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class UserMapper implements EntityMapper<UserDto, User> {

  @Mapping(source = "active", target = "isActive")
  public abstract User toEntity(UserDto dto, @Context CycleAvoidingMappingContext context);

  @Mapping(source = "active", target = "isActive")
  public abstract UserDto toDto(User entity, @Context CycleAvoidingMappingContext context);


}

