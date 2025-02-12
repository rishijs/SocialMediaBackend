package com.cooksys.social_media.dtos;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TweetResponseDto {
    private Long id;

    private UserResponseDto author;

    private String content;

    private Timestamp posted;

    private TweetResponseDto inReplyTo;

    private TweetResponseDto repostOf;

}