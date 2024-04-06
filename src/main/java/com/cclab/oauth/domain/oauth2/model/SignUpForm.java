package com.cclab.oauth.domain.oauth2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.objenesis.SpringObjenesis;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

    private String email;
    private String password;
    private String name;
    private String provider;
}
