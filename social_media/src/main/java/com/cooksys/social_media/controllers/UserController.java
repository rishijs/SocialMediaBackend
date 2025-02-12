package com.cooksys.social_media.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_media.dtos.CredentialsRequestDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.dtos.UserRequestDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.services.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    private List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/@{username}")
    private UserResponseDto getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @GetMapping("/@{username}/followers")
    private List<UserResponseDto> getFollowers(@PathVariable String username) {
        return userService.getFollowers(username);
    }

    @GetMapping("/@{username}/following")
    private List<UserResponseDto> getFollowing(@PathVariable String username) {
        return userService.getFollowing(username);
    }

    @GetMapping("/@{username}/mentions")
    private List<TweetResponseDto> getMentions(@PathVariable String username) {
        return userService.getMentions(username);
    }

    @GetMapping("/@{username}/tweets")
    private List<TweetResponseDto> getTweets(@PathVariable String username) {
        return userService.getTweets(username);
    }

    @GetMapping("/@{username}/feed")
    private List<TweetResponseDto> getFeed(@PathVariable String username) {
        return userService.getFeed(username);
    }

    @PostMapping
    private UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @PostMapping("/@{username}/follow")
    private void followUser(@PathVariable String username, @RequestBody CredentialsRequestDto credentialsRequestDto) {
        userService.followUser(username, credentialsRequestDto);
    }

    @PostMapping("/@{username}/unfollow")
    private void unfollowUser(@PathVariable String username, @RequestBody CredentialsRequestDto credentialsRequestDto) {
        userService.unfollowUser(username, credentialsRequestDto);
    }

    @PatchMapping("/@{username}")
    private UserResponseDto updateProfile(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
        return userService.updateProfile(username, userRequestDto);
    }

    @DeleteMapping("/@{username}")
    private UserResponseDto deleteUser(@PathVariable String username, @RequestBody CredentialsRequestDto credentialsRequestDto) {
        return userService.deleteUsername(username, credentialsRequestDto);
    }



}
