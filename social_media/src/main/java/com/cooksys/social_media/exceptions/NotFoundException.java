package com.cooksys.social_media.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NotFoundException extends RuntimeException {

    private static final long serializableVersionUID = 432562123051L;

    private String message;
}
