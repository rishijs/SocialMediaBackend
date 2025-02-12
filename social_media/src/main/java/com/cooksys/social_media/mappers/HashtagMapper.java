package com.cooksys.social_media.mappers;

import java.util.List;

import com.cooksys.social_media.dtos.HashtagResponseDto;
import com.cooksys.social_media.entities.Hashtag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    @Mapping(target = "tweets", ignore = true)
    Hashtag requestDtoToEntity(HashtagResponseDto dto);

    HashtagResponseDto entityToDto(Hashtag entity);

    List<HashtagResponseDto> entitiesToDtos(List<Hashtag> entities);

    List<Hashtag> dtoToEntities(List<HashtagResponseDto> stringToEntities);
}
