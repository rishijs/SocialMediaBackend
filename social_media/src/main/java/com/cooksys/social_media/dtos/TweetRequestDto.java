package com.cooksys.social_media.dtos;

import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetRequestDto {

    @Embedded
    private CredentialsRequestDto credentials;

    private String content;

}