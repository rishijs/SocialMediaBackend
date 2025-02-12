package com.cooksys.social_media.mappers;

import org.mapstruct.Mapper;

import com.cooksys.social_media.dtos.ProfileRequestDto;
import com.cooksys.social_media.embeddables.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    Profile requestDtoToEntity(ProfileRequestDto dto);

    ProfileRequestDto embeddableToDto(Profile embeddable);
}
