package com.cclab.oauth.security;

import com.cclab.oauth.domain.member.entity.Member;
import com.cclab.oauth.domain.member.repository.MemberRepository;
import com.cclab.oauth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> member = memberRepository.findByEmail(username);


        return member.map(it -> {

            UserDetailsImpl userDetails = UserDetailsImpl.builder()
                    .id(it.getMemberId())
                    .name(it.getNickname())
                    .email(it.getEmail())
                    .password(it.getPassword())
                    .build();

            return userDetails;

        }).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
