package com.cooksys.social_media.repositories;

import com.cooksys.social_media.dtos.TweetResponseDto;
import com.cooksys.social_media.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findAllByDeletedFalse();

    List<Tweet> findAllByInReplyTo(Tweet tweet);

    List<Tweet> findAllByRepostOfAndDeletedFalse(Tweet tweet);

    Optional<Tweet> findByIdAndDeletedFalse(long id);
}