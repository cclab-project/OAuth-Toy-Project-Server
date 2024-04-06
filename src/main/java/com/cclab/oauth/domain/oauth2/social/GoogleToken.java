package com.cclab.oauth.domain.oauth2.social;

import lombok.Getter;

@Getter
public class GoogleToken {

    private String access_token;
    private String expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
