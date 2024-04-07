package com.cclab.oauth.domain.jwt.helper;

import com.cclab.oauth.commom.error.TokenErrorCode;
import com.cclab.oauth.commom.exception.JwtException;
import com.cclab.oauth.domain.jwt.JwtDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class TokenHelper {

    @Value("${token.secret.key}")
    private String secretKey;

    @Value("${token.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("${token.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    public JwtDto issueAccessToken(Map<String, Object> data) {

        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(accessTokenPlusHour);

        Date expiredAt = Date.from(
                expiredLocalDateTime.atZone(
                        ZoneId.systemDefault()
                ).toInstant()
        );

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        String jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(data)
                .setExpiration(expiredAt)
                .compact();

        return JwtDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();

    }

    public JwtDto issueRefreshToken(Map<String, Object> data) {

        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(refreshTokenPlusHour);

        Date expiredAt = Date.from(
                expiredLocalDateTime.atZone(
                        ZoneId.systemDefault()
                ).toInstant()
        );

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        String jwtToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(data)
                .setExpiration(expiredAt)
                .compact();

        return JwtDto.builder()
                .token(jwtToken)
                .expiredAt(expiredLocalDateTime)
                .build();

    }

    public Map<String, Object> validationTokenWithThrow(String token) {

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        try {
            Jws<Claims> result = parser.parseClaimsJws(token);
            log.info("Jws result : {}", result.getBody());
            return new HashMap<String, Object>(result.getBody());

        } catch (Exception e) {

            if (e instanceof SignatureException) {
                throw new JwtException(TokenErrorCode.INVALID_TOKEN, e);
            }
            else if (e instanceof ExpiredJwtException) {
                throw new JwtException(TokenErrorCode.EXPIRED_TOKEN, e);
            }
            else {
                throw new JwtException(TokenErrorCode.TOKEN_EXCEPTION, e);
            }
        }
    }
}
