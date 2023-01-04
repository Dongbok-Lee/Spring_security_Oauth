package com.example.springsecurityjwt.common.security;

import com.example.springsecurityjwt.enumType.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import io.jsonwebtoken.*;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    @Value("app.auth.token-secret") private String secret;

    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 2l; // 2hours
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 30l; // 30days

    public String createAccessToken(String userId, AuthProvider provider, String accessToken){
        HashMap<String, Object> claim = new HashMap<>();
        claim.put("userId", userId);
        claim.put("provider", provider);
        claim.put("access_token", accessToken);
        return createJwt("ACCESS_TOKEN", ACCESS_TOKEN_EXPIRATION_TIME, claim);
    }

    public String createRefreshToken(String userId, AuthProvider provider, String refreshToken){
        HashMap<String, Object> claim = new HashMap<>();
        claim.put("userId", userId);
        claim.put("provider", provider);
        claim.put("refreshToken", refreshToken);
        return createJwt("REFRESH_TOKEN", REFRESH_TOKEN_EXPIRATION_TIME, claim);

    }

    public String createJwt(String subject, Long expiration, HashMap<String, Object> claim){
        JwtBuilder jwtBuilder = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(subject)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secret);

        if(claim != null)
            jwtBuilder.setClaims(claim);

        if(expiration != null)
            jwtBuilder.setExpiration(new Date(new Date().getTime() + expiration));

        return jwtBuilder.compact();
    }

    public Claims get(String jwt) throws JwtException{
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public boolean isExpiration(String jwt) throws JwtException{
        try{
            return get(jwt).getExpiration().before(new Date());
        }catch(ExpiredJwtException e){
            return true;
        }
    }

    public boolean canRefresh(String refreshToken) throws JwtException{
        Claims claims = get(refreshToken);
        long expirationTime = claims.getExpiration().getTime();
        long weekTime = 1000 * 60 * 60 * 24 * 7L;

        return new Date().getTime() > (expirationTime - weekTime);
    }
}
