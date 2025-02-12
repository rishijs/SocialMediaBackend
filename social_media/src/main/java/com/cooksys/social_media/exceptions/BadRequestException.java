package com.cooksys.social_media.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BadRequestException extends RuntimeException{

    private static final long serializableVersionUID = 435436114235051L;
    private String message;

}
