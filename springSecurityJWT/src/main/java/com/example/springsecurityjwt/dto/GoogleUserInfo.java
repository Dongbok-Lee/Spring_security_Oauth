package com.example.springsecurityjwt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoogleUserInfo {
    private String id;
    private String email;
    private String name;
    private String picture;
}
