package com.cooksys.social_media.services;

import com.cooksys.social_media.dtos.*;
import com.cooksys.social_media.embeddables.Credentials;
import com.cooksys.social_media.entities.Hashtag;

import java.util.List;

public interface TweetService {
    TweetResponseDto createTweet(TweetRequestDto tweet);

    TweetResponseDto createReply(long id,TweetRequestDto tweet);

    void createLike(long id,Credentials credentials);

    TweetResponseDto createRepost(long id, Credentials credentials);

    List<TweetResponseDto> getTweets();

    TweetResponseDto getTweet(long id);

    TweetResponseDto deleteTweet(long id,Credentials credentials);

    List<HashtagResponseDto> getTags(long id);

    List<UserResponseDto> getLikes(long id);

    ContextDto getContext(long id);

    List<TweetResponseDto> getReplies(long id);

    List<TweetResponseDto> getReposts(long id);

    List<UserResponseDto> getMentions(long id);
}