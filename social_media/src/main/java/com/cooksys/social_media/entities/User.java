package com.cooksys.social_media.entities;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.cooksys.social_media.embeddables.Credentials;
import com.cooksys.social_media.embeddables.Profile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_table")
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Credentials credentials;
    
    @CreationTimestamp
    private Timestamp joined;

    private boolean deleted;

    @Embedded
    private Profile profile;

    @OneToMany(mappedBy="author", cascade = CascadeType.ALL)
    private List<Tweet> tweets;
 
    @ManyToMany(mappedBy="likes", cascade = CascadeType.ALL)
    private List<Tweet> likes;

    @ManyToMany(mappedBy="mentions", cascade = CascadeType.ALL)
    private List<Tweet> mentions;

    @ManyToMany
    @JoinTable(
        name = "followers_following",
        joinColumns = @JoinColumn(name = "follower_id"),
        inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private List<User> following;

    @ManyToMany(mappedBy = "following")
    private List<User> followers;




}