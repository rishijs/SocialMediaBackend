package com.cooksys.social_media.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.social_media.embeddables.Credentials;
import com.cooksys.social_media.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCredentialsUsernameAndDeletedFalse(String username);

    List<User> findAllByDeletedFalse();

    Optional<User> findByCredentialsAndDeletedFalse(Credentials credentials);

    Optional<User> findByCredentialsUsername(String username);
}
