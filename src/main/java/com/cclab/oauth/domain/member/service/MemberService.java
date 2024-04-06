package com.cclab.oauth.domain.member.service;

import com.cclab.oauth.domain.member.entity.Member;
import com.cclab.oauth.domain.member.repository.MemberRepository;
import com.cclab.oauth.domain.oauth2.model.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member signUp(SignUpForm signUpForm) {
        if (memberRepository.existsByEmail(signUpForm.getEmail())) {
            throw new RuntimeException("이미 가입된 이메일");
        }

        Member member = Member.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getName())
                .provider(signUpForm.getProvider())
                .build();
        memberRepository.save(member);
        return member;
    }
}
