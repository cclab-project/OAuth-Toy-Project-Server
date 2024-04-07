package com.cclab.oauth.domain.jwt.service;

import com.cclab.oauth.commom.error.ErrorCode;
import com.cclab.oauth.commom.error.UserErrorCode;
import com.cclab.oauth.commom.exception.ApiException;
import com.cclab.oauth.domain.jwt.JwtDto;
import com.cclab.oauth.domain.jwt.helper.TokenHelper;
import com.cclab.oauth.domain.member.entity.Member;
import com.cclab.oauth.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenHelper tokenHelper;
    private final MemberRepository memberRepository;

    public JwtDto issueAccessToken(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        HashMap<String, Object> data = new HashMap<>();
        data.put("userId", memberId);
        data.put("userEmail", member.getEmail());
        //data.put("role", member.getRole());
        return tokenHelper.issueAccessToken(data);
    }

    public JwtDto issueRefreshToken(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
        HashMap<String, Object> data = new HashMap<>();
        data.put("userId", memberId);
        data.put("userEmail", member.getEmail());
        return tokenHelper.issueRefreshToken(data);
    }

    public String validationTokenWithUserEmail(String token){

        Map<String, Object> data = tokenHelper.validationTokenWithThrow(token);
        Object userEmail = data.get("userEmail");
        Objects.requireNonNull(userEmail, () -> {
            throw new ApiException(ErrorCode.NULL_POINT);
        });
        return userEmail.toString();
    }

    public boolean validationToken(String token, String username){
        final String tokenUsername = this.validationTokenWithUserEmail(token);
        return (username.equals(tokenUsername));
    }


}
