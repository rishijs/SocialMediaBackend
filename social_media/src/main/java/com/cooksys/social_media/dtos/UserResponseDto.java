package com.cooksys.social_media.dtos;

import java.sql.Timestamp;

import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserResponseDto {

  private String username;

  private Timestamp joined;

  @Embedded
  private ProfileRequestDto profile;

}
