package com.xstack.gymapp.model.mapper;

import org.mapstruct.Context;

public interface EntityMapper<D, E> {

  E toEntity(D dto, @Context CycleAvoidingMappingContext context);

  D toDto(E entity, @Context CycleAvoidingMappingContext context);

}
