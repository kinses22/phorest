package com.assessment.phorest.mapper;

public interface GenericMapper<DTO, Entity> {
    Entity mapToEntity(DTO dto);

}