package com.cooksys.social_media.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class Tweet {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @CreationTimestamp
    private Timestamp posted;

    private String content;

    @ManyToOne
    @JoinColumn(name = "reply_id", referencedColumnName = "id")
    private Tweet inReplyTo;

    @OneToMany(mappedBy = "inReplyTo")
    List<Tweet> replies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "repost_id", referencedColumnName = "id")
    private Tweet repostOf;

    @OneToMany(mappedBy = "repostOf")
    private List<Tweet> reposts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "tweet_hashtags",
        joinColumns = @JoinColumn(name = "tweet_id"),
        inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private List<Hashtag> hashtags;

    @ManyToMany
    @JoinTable(
        name = "tweet_mentions",
        joinColumns = @JoinColumn(name = "tweet_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> mentions;

    @ManyToMany
    @JoinTable(
        name = "tweet_likes",
        joinColumns = @JoinColumn(name = "tweet_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<User> likes;

    private boolean deleted;
}
