package com.cooksys.social_media.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.entities.User;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {

  @Mapping(target = "username", source = "credentials.username")
  UserResponseDto entityToResponseDto(User entity);

  List<User> dtoToEntities(List<UserRequestDto> dto);

  User requestDtoToEntity(UserRequestDto userRequestDto);

  List<UserResponseDto> entitiesToDtos(List<User> entities);
}
