package com.cclab.oauth.domain.oauth2;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Getter
public class Oauth2Attributes {

    private final String name;
    private final String email;

    @Builder
    public Oauth2Attributes(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public static Oauth2Attributes of(String registrationId, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(attributes);
        } else if ("google".equals(registrationId)) {
            return ofGoogle(attributes);
        }
        throw new RuntimeException("This provider does not support");
    }

    //google
    private static Oauth2Attributes ofGoogle(Map<String, Object> attributes) {
        Object googleEmail = attributes.get("email");
        Object googleName = attributes.get("name");

        return Oauth2Attributes.builder()
                .name(String.valueOf(googleName))
                .email(googleEmail.toString())
                .build();
    }

    //kakao
    private static Oauth2Attributes ofKakao(Map<String, Object> attributes) {

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) attributes.get("properties");

        return Oauth2Attributes.builder()
                .name(String.valueOf(kakaoProfile.get("nickname")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .build();
    }
}
