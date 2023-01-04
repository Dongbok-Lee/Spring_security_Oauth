package com.example.springsecurityjwt.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    GUEST("GUEST", "손님"),
    USER("USER", "일반 사용자");

    private final String key;
    private final String title;
}