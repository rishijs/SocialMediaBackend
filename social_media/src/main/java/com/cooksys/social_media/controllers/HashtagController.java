package com.cooksys.social_media.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.social_media.dtos.HashtagResponseDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.services.HashtagService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {
    private final HashtagService hashtagService;

    @GetMapping()
    public List<HashtagResponseDto> getAllTags(){
        return hashtagService.getAllTags();
    }

    @GetMapping("/{label}")
    public List<TweetResponseDto> getTags(@PathVariable String label){

        return hashtagService.getTags(label);
    }

}
