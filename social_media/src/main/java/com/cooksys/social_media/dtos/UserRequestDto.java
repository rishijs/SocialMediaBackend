package com.cooksys.social_media.dtos;

import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserRequestDto {

  @Embedded
  private CredentialsRequestDto credentials;

  @Embedded
  private ProfileRequestDto profile;

}