package com.example.springsecurityjwt.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    Guest("Guest", "손님"),
    User("USER", "일반 사용자");

    private final String key;
    private final String title;
}