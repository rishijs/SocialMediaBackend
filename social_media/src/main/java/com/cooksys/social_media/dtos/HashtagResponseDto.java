package com.cooksys.social_media.dtos;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HashtagResponseDto {
    private Long id;

    private String label;

    private Timestamp firstUsed;

    private Timestamp LastUsed;

}