package com.cclab.oauth.domain.member.service;

import com.cclab.oauth.commom.error.UserErrorCode;
import com.cclab.oauth.commom.exception.ApiException;
import com.cclab.oauth.domain.jwt.JwtDto;
import com.cclab.oauth.domain.jwt.TokenResponse;
import com.cclab.oauth.domain.jwt.service.TokenService;
import com.cclab.oauth.domain.member.entity.Member;
import com.cclab.oauth.domain.member.repository.MemberRepository;
import com.cclab.oauth.domain.oauth2.model.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    public JwtDto login(SignUpForm signUpForm) {
        if (memberRepository.existsByEmail(signUpForm.getEmail())) {
            Member member = memberRepository.findByEmail(signUpForm.getEmail()).orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
            TokenResponse tokenResponse = getTokenResponse(member);
            return tokenResponse.getAccessToken();
        }

        Member member = Member.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getName())
                .provider(signUpForm.getProvider())
                .build();
        Member savedMember = memberRepository.save(member);
        TokenResponse tokenResponse = getTokenResponse(savedMember);
        return tokenResponse.getAccessToken();
    }

    private TokenResponse getTokenResponse(Member member) {
        JwtDto accessToken = tokenService.issueAccessToken(member.getMemberId());
        JwtDto refreshToken = tokenService.issueRefreshToken(member.getMemberId());
        TokenResponse response = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return response;
    }
}
