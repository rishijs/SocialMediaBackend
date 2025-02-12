package com.cooksys.social_media;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cooksys.social_media.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {

  private final UserRepository userRepository;

  @Override
  public void run(String... args) throws Exception {
    
    //add to database

  }

}
