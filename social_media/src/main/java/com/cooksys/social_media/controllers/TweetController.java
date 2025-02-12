package com.cooksys.social_media.controllers;

import com.cooksys.social_media.dtos.*;
import com.cooksys.social_media.embeddables.Credentials;
import com.cooksys.social_media.entities.Hashtag;
import com.cooksys.social_media.services.TweetService;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    private final TweetService tweetService;

    @PostMapping
    public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweet){return tweetService.createTweet(tweet);}

    @PostMapping("/{id}/like")
    public void createLike(@PathVariable long id,@RequestBody Credentials credentials){
        tweetService.createLike(id,credentials);
    }

    @PostMapping("/{id}/reply")
    public TweetResponseDto createReply(@PathVariable long id,@RequestBody TweetRequestDto tweet){
        return tweetService.createReply(id,tweet);
    }

    @PostMapping("/{id}/repost")
    public TweetResponseDto createRepost(@PathVariable long id, @RequestBody Credentials credentials){
        return tweetService.createRepost(id,credentials);
    }

    @GetMapping
    public List<TweetResponseDto> getTweets(){return tweetService.getTweets();}

    @GetMapping("/{id}")
    public TweetResponseDto getTweet(@PathVariable long id){return tweetService.getTweet(id);}

    @DeleteMapping("/{id}")
    public TweetResponseDto deleteTweet(@PathVariable long id,@RequestBody Credentials credentials){
        return tweetService.deleteTweet(id,credentials);
    }

    @GetMapping("/{id}/tags")
    public List<HashtagResponseDto> getTags(@PathVariable long id){return tweetService.getTags(id);}

    @GetMapping("/{id}/likes")
    public List<UserResponseDto> getLikes(@PathVariable long id){return tweetService.getLikes(id);}

    @GetMapping("/{id}/context")
    public ContextDto getContext(@PathVariable long id){return tweetService.getContext(id);}

    @GetMapping("/{id}/replies")
    public List<TweetResponseDto> getReplies(@PathVariable long id){return tweetService.getReplies(id);}

    @GetMapping("/{id}/reposts")
    public List<TweetResponseDto> getReposts(@PathVariable long id){return tweetService.getReposts(id);}

    @GetMapping("/{id}/mentions")
    public List<UserResponseDto> getMentions(@PathVariable long id){return tweetService.getMentions(id);}
}