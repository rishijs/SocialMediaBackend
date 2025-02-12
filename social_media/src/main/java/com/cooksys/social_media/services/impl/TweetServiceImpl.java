package com.cooksys.social_media.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_media.dtos.ContextDto;
import com.cooksys.social_media.dtos.HashtagResponseDto;
import com.cooksys.social_media.dtos.TweetRequestDto;
import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.dtos.UserResponseDto;
import com.cooksys.social_media.embeddables.Credentials;
import com.cooksys.social_media.entities.Hashtag;
import com.cooksys.social_media.entities.Tweet;
import com.cooksys.social_media.entities.User;
import com.cooksys.social_media.exceptions.BadRequestException;
import com.cooksys.social_media.exceptions.NotAuthorizedException;
import com.cooksys.social_media.exceptions.NotFoundException;
import com.cooksys.social_media.mappers.CredentialsMapper;
import com.cooksys.social_media.mappers.HashtagMapper;
import com.cooksys.social_media.mappers.TweetMapper;
import com.cooksys.social_media.mappers.UserMapper;
import com.cooksys.social_media.repositories.HashtagRepository;
import com.cooksys.social_media.repositories.TweetRepository;
import com.cooksys.social_media.repositories.UserRepository;
import com.cooksys.social_media.services.TweetService;

import lombok.RequiredArgsConstructor;

import javax.swing.text.html.Option;


@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;
    private final CredentialsMapper credentialsMapper;
    private final UserMapper userMapper;
    private final HashtagMapper hashtagMapper;


    private Tweet handleTagsAndMentions(Tweet entity){
        List<Hashtag> hashtagList = new ArrayList<>();
        List<User> userList = new ArrayList<>();

        String content = entity.getContent();
        //pulltags out of content
        List<String> hashtags = processString(content,"#");
        //System.out.println(hashtags)

        //For each tag check if exists and if so update last time used
        for(String item : hashtags){
            Optional<Hashtag> tag = hashtagRepository.findByLabel(item);
            if (tag.isEmpty()){
                Hashtag temp = new Hashtag();
                temp.setLabel(item);
                ArrayList<Tweet> tempList = new ArrayList<>();
                tempList.add(entity);
                temp.setTweets(tempList);
                hashtagList.add(temp);
            }else{
                tag.get().setLastUsed(new Timestamp(System.currentTimeMillis()));
            }
        }
        //set tweet's hashtags and save
        entity.setHashtags(hashtagList);
        hashtagRepository.saveAll(hashtagList);

        List<String> mentions = processString(content,"@");
        //System.out.println(mentions);


        //For each mention check if user exists and then add tweet to their mentions vice versa
        for(String item : mentions){
            Optional<User> temp = userRepository.findByCredentialsUsernameAndDeletedFalse(item);
            if(temp.isPresent()) {
                userList.add(temp.get());
                temp.get().getMentions().add(entity);
                userRepository.save(temp.get());
            }
        }
        entity.setMentions(userList);
        tweetRepository.save(entity);
        return entity;
    }

    /**
     * Get User given a credentials from request
     * @param credentials from req body
     * @return user with those credentials
     */
    private User getUserByCred(Credentials credentials){
        Optional<User> user = userRepository.findByCredentialsAndDeletedFalse(credentials);
        if(user.isPresent()){
            return user.get();
        }else{
            throw new NotFoundException("this user does not exist");
        }
    }

    /**
     * Get Tweet by ID
     * @param id id of tweet
     * @return Tweet with given ID
     */
    private Tweet getTweetById(long id){
        Optional<Tweet> tweet = tweetRepository.findByIdAndDeletedFalse(id);
        if (tweet.isPresent()){
            if (tweet.get().isDeleted()){
                throw new BadRequestException("This tweet is already deleted");
            }
            return tweet.get();
        }else{
            throw new NotFoundException("Tweet does not exist.");
        }
    }

    /**
     * Given a tweet's content and a option of # or @ pull the users and tags from the content
     * @param content tweet's content
     * @param atOrHashtag '#' or '@'
     * @return all '#''s or '@''s
     */
    private List<String> processString(String content,String atOrHashtag){
        ArrayList<String> returnList = new ArrayList<>();
        int startIndex = content.indexOf(atOrHashtag);
        int currIndex = 0;
        while(currIndex != -1){
            int[] indicies =
            {
             content.indexOf(" ",startIndex + 1) - startIndex
            ,content.indexOf(",", startIndex + 1)- startIndex
            ,content.indexOf("#", startIndex + 1)- startIndex
            ,content.indexOf("@", startIndex + 1)- startIndex
            ,content.length() - startIndex
            };
            int minimum = 999999;
            for (int index: indicies) {
                if(index < minimum && index > -1){
                    minimum = index;
                }
            }
            if(minimum == 999999 || startIndex < 0){
                currIndex = -1;
            } else{
                returnList.add(content.substring(startIndex + 1,startIndex + minimum));
                currIndex = content.indexOf(atOrHashtag,startIndex+minimum+1);
                startIndex = startIndex+minimum+1;
            }
        }
        return returnList;
    }


    /**
     * Create a Tweet, upon doing so tags and mentions should also be handled.
     * @param tweet tweet Request Dto includes content and credentials
     * @return New Tweet
     */
    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweet) {
        //if no content in body
        if(tweet.getContent() == null){throw new BadRequestException("Content cannot be null");}
        //if no credentials in body
        if(tweet.getCredentials() == null){throw new BadRequestException("Bad Credentials");}
        //if no password
        if(tweet.getCredentials().getPassword() == null || tweet.getCredentials().getPassword().equals("")){
            throw new BadRequestException("Bad Credentials");
        }
        //if no username
        if(tweet.getCredentials().getUsername() == null || tweet.getCredentials().getUsername().equals("")){
            throw new BadRequestException("Bad Credentials");
        }
        //Create Tweet entity
        Tweet entity = tweetMapper.requestDtoToEntity(tweet);
        tweetRepository.saveAndFlush(entity);

        //Ensure User exists and set author of tweet as well as add tweet to user.
        User user = getUserByCred(credentialsMapper.requestDtoToEntity(tweet.getCredentials()));
        entity.setAuthor(user);
        user.getTweets().add(entity);
        userRepository.saveAndFlush(user);
        tweetRepository.saveAndFlush(entity);

        //finally handle the tags and mentions associated with the content of the tweet.
        handleTagsAndMentions(entity);

        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(entity));
    }


    /**
     * Given a tweet id create a new tweet InReplyTo that tweet
     * @param id id of target to respond to
     * @param tweet tweet to respond with
     * @return new Tweet in response.
     */
    @Override
    public TweetResponseDto createReply(long id,TweetRequestDto tweet) {
        Tweet top = getTweetById(id);
        Tweet reply = tweetMapper.requestDtoToEntity(tweet);
        reply.setInReplyTo(top);
        handleTagsAndMentions(reply);

        User user = getUserByCred(credentialsMapper.requestDtoToEntity(tweet.getCredentials()));
        reply.setAuthor(user);
        user.getTweets().add(reply);
        userRepository.saveAndFlush(user);


        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(reply));
    }

    /**
     * Create a new like relationship between a user and a tweet
     * @param id id of tweet
     * @param credentials credentials of user to like the tweet.
     */
    @Override
    public void createLike(long id,Credentials credentials) {
        Tweet tweet = getTweetById(id);
        User user = getUserByCred(credentials);
        if(!user.getLikes().contains(tweet)) {
            user.getLikes().add(tweet);
            tweet.getLikes().add(user);
        }
        userRepository.saveAllAndFlush(tweet.getLikes());
    }

    /**
     * Creates a new tweet that is a repost of the targeted tweet from given id
     * @param id of target
     * @param credentials of user to repost
     * @return new Tweet in repostOf
     */
    @Override
    public TweetResponseDto createRepost(long id, Credentials credentials) {
        //Get user and Tweet
        Tweet tweet = getTweetById(id);
        User user = getUserByCred(credentials);

        //Create new Request dto out of the target tweet
        TweetRequestDto req = tweetMapper.entityToRequestDto(tweet);
        //update user
        req.setCredentials(credentialsMapper.entityToReqDto(user.getCredentials()));
        //ensure content is properly set
        req.setContent(tweet.getContent());
        //Create entity
        Tweet tweetToSave = tweetMapper.requestDtoToEntity(req);

        tweetToSave.setRepostOf(tweet);
        tweetToSave.setAuthor(user);

        //handle tags and mentions to ensure object is created properly
        handleTagsAndMentions(tweetToSave);

        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToSave));
    }

    /**
     * @return All non-deleted tweets
     */
    @Override
    public List<TweetResponseDto> getTweets() {
        return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalse());
    }

    /**
     *
     * @param id id of target
     * @return Tweet given id
     */
    @Override
    public TweetResponseDto getTweet(long id) {
        return tweetMapper.entityToDto(getTweetById(id));
    }

    /**
     * Update DB to reflect Tweet as deleted if the user owns the tweet
     * @param id of target tweet
     * @param credentials of target user
     * @return deleted tweet
     */
    @Override
    public TweetResponseDto deleteTweet(long id,Credentials credentials) {
        Tweet tweet = getTweetById(id);
        User user = getUserByCred(credentials);
        if(!tweet.getAuthor().getProfile().equals(user.getProfile())){
            throw new NotAuthorizedException("This user does not own this tweet");
        }else{
            tweet.setDeleted(true);
            return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweet));
        }
    }

    /**
     * Get all Tags from one tweet
     * @param id target tweet id
     * @return List of Hashtags
     */
    @Override
    public List<HashtagResponseDto> getTags(long id) {
        Tweet tweet = getTweetById(id);
        List<Hashtag> tags = tweet.getHashtags();

        return hashtagMapper.entitiesToDtos(tags);
    }

    /**
     * Get All likes
     * @param id target Tweet id
     * @return List of User's who have liked this tweet
     */
    @Override
    public List<UserResponseDto> getLikes(long id) {
        Tweet tweet = getTweetById(id);
        List<User> users = tweet.getLikes();
        return userMapper.entitiesToDtos(users);
    }
    //Helper method to return a List of tweet's in reply to the parameter tweet
    private List<Tweet> getAllReplies(Tweet tweet){
        return tweetRepository.findAllByInReplyTo(tweet);
    }

    /**
     * Return All replies including chained replies of a reply
     * @param tweet target tweet
     * @param replies a running list of replies that is given and recursively passed and updated
     */
    private void getAllRepliesCont(Tweet tweet,List<Tweet> replies){
        List<Tweet> currReplies = getAllReplies(tweet);
        replies.addAll(currReplies);
        for(Tweet item :currReplies){
            getAllRepliesCont(item,replies);
        }
    }

    /**
     * Return All Prior Replies of a tweet
     * @param tweet target Tweet
     * @param before A list of replies this tweet is in Reply to
     */
    private void getAllRepliesBef(Tweet tweet,List<Tweet> before){
        Tweet temp = tweet;
        while(temp.getInReplyTo() != null) {
            before.add(temp.getInReplyTo());
            temp = temp.getInReplyTo();
        }
    }

    /**
     * Combine the previous two methods results into one contextDto
     * @param id target tweet to get Context of
     * @return ContextDto with before, after, and target
     */
    @Override
    public ContextDto getContext(long id) {
        Tweet tweet = getTweetById(id);
        //Create new lists to be passed to methods.
        List<Tweet> before = new ArrayList<>();
        List<Tweet> after = new ArrayList<>();
        getAllRepliesCont(tweet, after);
        getAllRepliesBef(tweet,before);

        return new ContextDto(tweetMapper.entityToDto(tweet),tweetMapper.entitiesToDtos(before),tweetMapper.entitiesToDtos(after));
    }

    /**
     * Get All replies of a certain tweet
     * @param id target tweet
     * @return List of Tweet replies
     */
    @Override
    public List<TweetResponseDto> getReplies(long id) {
        Tweet tweet = getTweetById(id);
        List<Tweet> replies = getAllReplies(tweet);
        return tweetMapper.entitiesToDtos(replies);
    }

    /**
     * Gets all tweets that are reposts of the target id
     * @param id of target tweet
     * @return List of Tweet's that are reposts of the target
     */
    @Override
    public List<TweetResponseDto> getReposts(long id) {
        Tweet tweet = getTweetById(id);
        List<Tweet> reposts = tweetRepository.findAllByRepostOfAndDeletedFalse(tweet);
        return tweetMapper.entitiesToDtos(reposts);
    }

    /**
     * Gets All User's mentioned in a tweet.
     * @param id of Tweet
     * @return List of User's
     */
    @Override
    public List<UserResponseDto> getMentions(long id) {
        Tweet tweet = getTweetById(id);
        List<User> mentions = tweet.getMentions();
        mentions.removeIf(User::isDeleted);
        return userMapper.entitiesToDtos(mentions);
    }


}