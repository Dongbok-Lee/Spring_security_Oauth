package com.example.springsecurityjwt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.context.annotation.Profile;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoUserInfo {
    private Long id;
    private KakaoAccount kakaoAccount;

    @Getter
    private static class KakaoAccount{
        private String email;
        private Profile profile;

        @Getter
        private static class Profile{
            private String nickname;
            private String profileImageUrl;
        }
    }
}
