package com.cooksys.social_media.services;

import java.util.List;

import com.cooksys.social_media.dtos.CredentialsRequestDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;

public interface UserService {

    List<UserResponseDto> getAllUsers();
    UserResponseDto getUser(String username);
    List<UserResponseDto> getFollowers(String username);
    List<UserResponseDto> getFollowing(String username);
    List<TweetResponseDto> getMentions(String username);
    List<TweetResponseDto> getTweets(String username);
    List<TweetResponseDto> getFeed(String username);
    UserResponseDto createUser(UserRequestDto userRequestDto);
    void followUser(String username, CredentialsRequestDto credentialsRequestDto);
    void unfollowUser(String username, CredentialsRequestDto credentialsRequestDto);
    UserResponseDto updateProfile(String username, UserRequestDto userRequestDto);
    UserResponseDto deleteUsername(String username, CredentialsRequestDto credentialsRequestDto);

}