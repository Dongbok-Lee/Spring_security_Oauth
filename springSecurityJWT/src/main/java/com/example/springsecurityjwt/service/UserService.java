package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.common.exception.BadRequestException;
import com.example.springsecurityjwt.dto.SignUpRequest;
import com.example.springsecurityjwt.entity.User;
import com.example.springsecurityjwt.enumType.Role;
import com.example.springsecurityjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public String createUser(SignUpRequest signUpRequest){
        if(userRepository.existsByIdAndAuthProvider(signUpRequest.getId(), signUpRequest.getAuthProvider())){
            throw new BadRequestException("already exist user");
        }

        return userRepository.save(
                User.builder()
                        .id(signUpRequest.getId())
                        .nickname(signUpRequest.getNickname())
                        .email(signUpRequest.getEmail())
                        .profileImageUrl(signUpRequest.getProfileImageUrl())
                        .role(Role.USER)
                        .authProvider(signUpRequest.getAuthProvider())
                        .build()
        ).getId();
    }

}
