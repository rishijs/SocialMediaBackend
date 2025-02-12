package com.cooksys.social_media.mappers;

import org.mapstruct.Mapper;

import com.cooksys.social_media.dtos.CredentialsRequestDto;
import com.cooksys.social_media.embeddables.Credentials;

@Mapper(componentModel = "spring")
public interface CredentialsMapper {
  
  Credentials requestDtoToEntity(CredentialsRequestDto credentialsRequestDto);

  CredentialsRequestDto entityToReqDto(Credentials credentials);
}