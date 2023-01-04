package com.example.springsecurityjwt.entity;

import com.example.springsecurityjwt.enumType.AuthProvider;
import com.example.springsecurityjwt.enumType.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity{
    @Id
    private String id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider;

    @Builder
    public User(String id, String nickname, String email, String profileImageUrl, Role role, AuthProvider authProvider) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.authProvider = authProvider;
    }

    public User update(String name, String picture){
        this.nickname = name;
        this.profileImageUrl = picture;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
