package com.cooksys.social_media.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_media.dtos.CredentialsRequestDto;
import com.cooksys.social_media.dtos.ProfileRequestDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.entities.Tweet;
import com.cooksys.social_media.entities.User;
import com.cooksys.social_media.exceptions.BadRequestException;
import com.cooksys.social_media.exceptions.NotAuthorizedException;
import com.cooksys.social_media.exceptions.NotFoundException;
import com.cooksys.social_media.mappers.CredentialsMapper;
import com.cooksys.social_media.mappers.ProfileMapper;
import com.cooksys.social_media.mappers.TweetMapper;
import com.cooksys.social_media.mappers.UserMapper;
import com.cooksys.social_media.repositories.UserRepository;
import com.cooksys.social_media.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final TweetMapper tweetMapper;
  private final CredentialsMapper credentialsMapper;
  private final ProfileMapper profileMapper;
  
  // must find a user, else throws an error
  private User validateUserWithUsername(String username){
    Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
    if (optionalUser.isPresent()){
      if (optionalUser.get().isDeleted()){
        throw new BadRequestException(String.format("User with username %s was deleted", username));
      }
      return optionalUser.get();
    }
    throw new NotFoundException(String.format("User with username %s was not found", username));
  }

  // returns a non deleted user if found without throwing errors
  private User getUserWithUsername(String username){
    Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
    if (optionalUser.isPresent() && !optionalUser.get().isDeleted()){
      return optionalUser.get();
    }
    return null;
  }

  // returns any user if found without throwing errors
  private User getAnyUserWithUsername(String username){
    Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
    if (optionalUser.isPresent()){
      return optionalUser.get();
    }
    return null;
  }

  
  // verify that the credentials match the username -> user that was requested
  private void isMatchingCredentials(User currentUser, CredentialsRequestDto credentialsRequestDto){
    if (!currentUser.getCredentials().equals(credentialsMapper.requestDtoToEntity(credentialsRequestDto))){
      throw new NotAuthorizedException("User credentials do not match credentials request");
    }
  }

  // checks whether the credentials request has valid fields
  private void isValidCredentials(CredentialsRequestDto credentialsRequestDto){
    if (credentialsRequestDto == null){
      throw new BadRequestException(String.format("Credentials are missing in user request"));
    }
    if (credentialsRequestDto.getUsername() == null || credentialsRequestDto.getPassword() == null){
      throw new BadRequestException(String.format("Either username or password are missing in credentials request"));
    }
  }

  // checks whether the profile request has valid fields
  private void isValidProfile(ProfileRequestDto profileRequestDto){
    if (profileRequestDto == null){
      throw new BadRequestException(String.format("Profile is missing in user request"));
    }
    if (profileRequestDto.getFirstName() == null || profileRequestDto.getLastName() == null || profileRequestDto.getEmail() == null ||
    profileRequestDto.getPhone() == null){
      throw new BadRequestException(String.format("A field is missing in profile request"));
    }
  }

  // combines all checks to find if user request has all fields
  private void isValidUserRequest(UserRequestDto userRequestDto){
    isValidCredentials(userRequestDto.getCredentials());
    isValidProfile(userRequestDto.getProfile());
  }


  // get all users
  @Override
  public List<UserResponseDto> getAllUsers() {
    return userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse());
  }


  // get a specific user
  @Override
  public UserResponseDto getUser(String username) {
    User currentUser = validateUserWithUsername(username);
    return userMapper.entityToResponseDto(userRepository.save(currentUser));
  }


  // get the users a specific user is following
  @Override
  public List<UserResponseDto> getFollowers(String username) {
    User currentUser = validateUserWithUsername(username);
    List<User> followers = new ArrayList<>();

    // if the user is not deleted and is part of user's current followers
    for (User follower: currentUser.getFollowers()){
      if (!follower.isDeleted()){
        followers.add(follower);
      }
    }
    return userMapper.entitiesToDtos(followers);
  }

  // get the followers of a specific user
  @Override
  public List<UserResponseDto> getFollowing(String username) {
    User currentUser = validateUserWithUsername(username);
    List<User> following = new ArrayList<>();

    // if the user is not deleted and is part of user's current following
    for (User follower: currentUser.getFollowing()){
      if (!follower.isDeleted()){
        following.add(follower);
      }
    }
    return userMapper.entitiesToDtos(following);
  }


  // get the undeleted tweets where the specific user is mentioned
  @Override
  public List<TweetResponseDto> getMentions(String username) {
    User currentUser = validateUserWithUsername(username);
    List<Tweet> mentions = new ArrayList<>();
    for (Tweet mention: currentUser.getMentions()){
      if (!mention.isDeleted()){
        mentions.add(mention);
      }
    }
    return tweetMapper.entitiesToDtos(mentions);
  }

  // get the undeleted tweets of a specific user
  @Override
  public List<TweetResponseDto> getTweets(String username) {
    User currentUser = validateUserWithUsername(username);
    List<Tweet> tweets = new ArrayList<>();
    for (Tweet tweet: currentUser.getTweets()){
      if (!tweet.isDeleted()){
        tweets.add(tweet);
      }
    }
    return tweetMapper.entitiesToDtos(tweets);
  }

  // get the curated content for a specific user with additional check of whether the feed already contains the tweets
  @Override
  public List<TweetResponseDto> getFeed(String username) {
    User currentUser = validateUserWithUsername(username);
    List<Tweet> feed = new ArrayList<>();

    // user undeleted mentions
    for (Tweet tweet: currentUser.getMentions()){
      if (!tweet.isDeleted() && !feed.contains(tweet)){
        feed.add(tweet);
      }
    }

    // user undeleted tweets
    for (Tweet tweet: currentUser.getTweets()){
      if (!tweet.isDeleted() && !feed.contains(tweet)){
        feed.add(tweet);
      }
    }

    // user undeleted likes
    for (Tweet tweet: currentUser.getLikes()){
      if (!tweet.isDeleted() && !feed.contains(tweet)){
        feed.add(tweet);
      }
    }
    

    // user's following's undeleted likes and tweets
    for (User user: currentUser.getFollowing()){
      for (Tweet tweet: user.getTweets()){
        if (!tweet.isDeleted() && !feed.contains(tweet)){
          feed.add(tweet);
        }
      }
      for (Tweet tweet: user.getLikes()){
        if (!tweet.isDeleted() && !feed.contains(tweet)){
          feed.add(tweet);
        }
      }
    }

    return tweetMapper.entitiesToDtos(feed);
  }


  // creates a user with the given request or reactives a user if the username is already present and deleted
  @Override
  public UserResponseDto createUser(UserRequestDto userRequestDto) {
    // finds and returns a user with a username if the user is not deleted
    isValidCredentials(userRequestDto.getCredentials());
    User currentUser = getAnyUserWithUsername(userRequestDto.getCredentials().getUsername());
    // user is deleted or does not exist
    if (currentUser == null){
      // check whether fields are null
      isValidUserRequest(userRequestDto);

      // nested replace and create user
      // a user request only contains credentials and profile
      User newUser = userRepository.save(userMapper.requestDtoToEntity(userRequestDto));
      newUser.setDeleted(false);
      newUser.setCredentials(credentialsMapper.requestDtoToEntity(userRequestDto.getCredentials()));
      newUser.setProfile(profileMapper.requestDtoToEntity(userRequestDto.getProfile()));
      return userMapper.entityToResponseDto(userRepository.save(newUser));
    }

    // user is deleted and needs to be reactivated
    else{
      isMatchingCredentials(currentUser, userRequestDto.getCredentials());
      if (currentUser.isDeleted()){
        currentUser.setDeleted(false);
        return userMapper.entityToResponseDto(userRepository.save(currentUser));
      }
      throw new BadRequestException(String.format("User with username %s is already created", userRequestDto.getCredentials().getUsername()));
    }

  }

  // follow a specific user with a username
  @Override
  public void followUser(String username, CredentialsRequestDto credentialsRequestDto) {
    isValidCredentials(credentialsRequestDto);
    User userToFollow = validateUserWithUsername(username);
    User currentUser = validateUserWithUsername(credentialsRequestDto.getUsername());
    isMatchingCredentials(currentUser, credentialsRequestDto);

    if (currentUser.getFollowing().contains(userToFollow)){
      throw new BadRequestException(String.format("User %s is already following user %s", 
      currentUser.getCredentials().getUsername(), 
      userToFollow.getCredentials().getUsername()));
    }

    currentUser.getFollowing().add(userToFollow);
    userToFollow.getFollowers().add(currentUser);
    userRepository.save(currentUser);
    userRepository.save(userToFollow);

  }

  // unfollow a specific user with a username
  @Override
  public void unfollowUser(String username, CredentialsRequestDto credentialsRequestDto) {
    isValidCredentials(credentialsRequestDto);
    User userToFollow = validateUserWithUsername(username);
    User currentUser = validateUserWithUsername(credentialsRequestDto.getUsername());
    isMatchingCredentials(currentUser, credentialsRequestDto);
    
    if (!currentUser.getFollowing().contains(userToFollow)){
      throw new BadRequestException(String.format("User %s is not following user %s", 
      currentUser.getCredentials().getUsername(), 
      userToFollow.getCredentials().getUsername()));
    }
    
    currentUser.getFollowing().remove(userToFollow);
    userToFollow.getFollowers().remove(currentUser);
    userRepository.save(currentUser);
    userRepository.save(userToFollow);
  }

  // changes a specific user's profile
  @Override
  public UserResponseDto updateProfile(String username, UserRequestDto userRequestDto) {
    User currentUser = validateUserWithUsername(username);
    isValidCredentials(userRequestDto.getCredentials());
    isMatchingCredentials(currentUser, userRequestDto.getCredentials());
    if (userRequestDto.getProfile() == null){
      throw new BadRequestException(String.format("Profile is missing in user request"));
    }


    // checking individual fields if they are null, do not replace a non-null field with a null field
    if (userRequestDto.getProfile().getFirstName() != null){
      currentUser.getProfile().setFirstName(userRequestDto.getProfile().getFirstName());
    }
    if (userRequestDto.getProfile().getLastName() != null){
      currentUser.getProfile().setLastName(userRequestDto.getProfile().getLastName());
    } 
    if (userRequestDto.getProfile().getEmail() != null){
      currentUser.getProfile().setEmail(userRequestDto.getProfile().getEmail());
    } 
    if (userRequestDto.getProfile().getPhone() != null){
      currentUser.getProfile().setPhone(userRequestDto.getProfile().getPhone());
    }

    return userMapper.entityToResponseDto(userRepository.save(currentUser));
  }


  // deletes a specific user
  @Override
  public UserResponseDto deleteUsername(String username, CredentialsRequestDto credentialsRequestDto) {
    User currentUser = validateUserWithUsername(username);
    isMatchingCredentials(currentUser, credentialsRequestDto);
    User originalCopy = currentUser;
    currentUser.setDeleted(true);
    // tweets are not deleted on user deletion
    userRepository.save(currentUser);
    return userMapper.entityToResponseDto(originalCopy);
  }

}
