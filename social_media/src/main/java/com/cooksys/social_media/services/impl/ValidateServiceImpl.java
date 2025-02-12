package com.cooksys.social_media.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.social_media.entities.Hashtag;
import com.cooksys.social_media.entities.User;
import com.cooksys.social_media.repositories.HashtagRepository;
import com.cooksys.social_media.repositories.UserRepository;
import com.cooksys.social_media.services.ValidateService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
    private final HashtagRepository hashtagRepository;
    private final UserRepository userRepository;

    /**
     * Determines if a tag has been created
     * @param label the tag to check
     * @return T/F
     */
    @Override
    public boolean checkIfTagExists(String label) {
        Optional<Hashtag> tag = hashtagRepository.findByLabel(label);
        return tag.isPresent();
    }

    @Override
    public boolean checkIfUsernameExists(String username) {
        Optional<User> user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        return user.isPresent();
    }

    @Override
    public boolean checkIfUsernameAvailable(String username) {
        Optional<User> user = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
        return user.isEmpty();
    }
}