package com.cclab.oauth.domain.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {

    JwtDto accessToken;
    JwtDto refreshToken;
}
