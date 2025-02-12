package com.cooksys.social_media.services;


import java.util.List;

import com.cooksys.social_media.dtos.HashtagResponseDto;
import com.cooksys.social_media.dtos.TweetResponseDto;

public interface HashtagService {
    List<HashtagResponseDto> getAllTags();


    List<TweetResponseDto> getTags(String label);
}