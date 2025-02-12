package com.cooksys.social_media.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_media.dtos.HashtagResponseDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.entities.Hashtag;
import com.cooksys.social_media.entities.Tweet;
import com.cooksys.social_media.exceptions.BadRequestException;
import com.cooksys.social_media.mappers.HashtagMapper;
import com.cooksys.social_media.mappers.TweetMapper;
import com.cooksys.social_media.repositories.HashtagRepository;
import com.cooksys.social_media.repositories.TweetRepository;
import com.cooksys.social_media.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {
    //WHEN CREATING HASHTAG LABEL MUST BE UNIQUE
    private final TweetRepository tweetRepository;
    private final HashtagRepository hashtagRepository;
    private final TweetMapper tweetMapper;
    private final HashtagMapper hashtagMapper;

    /**
     * Returns all used tags
     * @return a list of Hashtags
     */
    @Override
    public List<HashtagResponseDto> getAllTags() {
        return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
    }

    /**
     * Get All tweets that have mentioned this tag
     * @param label
     * @return
     */
    @Override
    public List<TweetResponseDto> getTags(String label) {
        // look for a tweet with matching label
        Optional<Hashtag> tag = hashtagRepository.findByLabel(label);
        if (tag.isEmpty()){
            throw new BadRequestException(String.format("Tag %s does not exist",label));
        }

        // get all tweets with the matching tag
        List<Tweet> allMatchingTweets = new ArrayList<>();
        List<Tweet> tweets = tweetRepository.findAllByDeletedFalse();
        for (Tweet t:tweets){
            if(t.getHashtags().contains(tag.get())){
                allMatchingTweets.add(t);
            }
        }
        return tweetMapper.entitiesToDtos(allMatchingTweets);
    }
}