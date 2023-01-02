package com.example.springsecurityjwt.common.security;

import com.example.springsecurityjwt.common.exception.BadRequestException;
import com.example.springsecurityjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.AuthProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain){
        log.debug("**** SECURITY FILTER START ****");

        try{
            String authorizationHeader = request.getHeader("Authorization");
            String token;
            String userId;
            String provider;

            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                token = authorizationHeader.substring(7);

                if(securityUtil.isExpiration(token))
                    throw new BadRequestException("Expired_ACCESS_TOKEN");
            }

            userId = (String) securityUtil.get(token).get("userId");
            provider = (String) securityUtil.get(token).get("provider");

            if(!userRepository.existsByIdAndAuthProvider(userId, AuthProvider.findByCode(provider))){
                throw new BadRequestException("CANNOT_FOUND_USER");
            }
        }
        filterChain.doFilter(request, response);
    }
}
