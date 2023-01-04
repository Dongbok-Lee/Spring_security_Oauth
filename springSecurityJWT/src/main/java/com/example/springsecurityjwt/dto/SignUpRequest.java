package com.example.springsecurityjwt.dto;

import com.example.springsecurityjwt.enumType.AuthProvider;
import lombok.Getter;

@Getter
public class SignUpRequest {
    private String id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private AuthProvider authProvider;
}
