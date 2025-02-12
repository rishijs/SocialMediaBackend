package com.cooksys.social_media.services;

public interface ValidateService {
    boolean checkIfTagExists(String label);
    boolean checkIfUsernameExists(String username);
    boolean checkIfUsernameAvailable(String username);
}