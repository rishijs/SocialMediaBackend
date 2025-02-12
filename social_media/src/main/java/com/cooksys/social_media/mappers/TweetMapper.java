package com.cooksys.social_media.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.social_media.dtos.ContextDto;
import com.cooksys.social_media.dtos.TweetRequestDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.entities.Tweet;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TweetMapper {

    Tweet requestDtoToEntity(TweetRequestDto dto);

    @Mapping(target = "credentials", ignore = true)
    TweetRequestDto entityToRequestDto(Tweet tweet);

    TweetResponseDto entityToDto(Tweet entity);

    List<TweetResponseDto> entitiesToDtos(List<Tweet> entities);

}
