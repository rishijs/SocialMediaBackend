package com.cooksys.social_media.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotAuthorizedException extends RuntimeException{

    private static final long serializableVersionUID = 432512314235051L;
    private String message;

}
