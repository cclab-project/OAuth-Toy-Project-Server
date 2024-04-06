package com.cclab.oauth.authentication;

import com.cclab.oauth.domain.jwt.JwtDto;
import com.cclab.oauth.domain.member.entity.Member;
import com.cclab.oauth.domain.member.service.MemberService;
import com.cclab.oauth.domain.oauth2.model.SignUpForm;
import com.cclab.oauth.domain.oauth2.service.SocialLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

    private final SocialLoginService socialLoginService;
    private final MemberService memberService;

    @GetMapping("/oauth2/{provider}")
    public void tryOauth2(@PathVariable String provider, HttpServletResponse response) throws IOException {
        String url = socialLoginService.tryOauth2(provider);
        log.info("created url = {}", url);
        response.sendRedirect(url);
    }

    @GetMapping("/login/oauth2/code/{provider}")
    public ResponseEntity<JwtDto> authorized(@PathVariable String provider, @RequestParam String code) {
        log.info("authorized - provider : {}, code : {}", provider, code);
        return socialLoginService.connectToSocialLogin(provider, code);
    }

    @PostMapping("/login/social/{provider}")
    public ResponseEntity<JwtDto> socialSignIn(@PathVariable String provider, @RequestBody String code) {
        log.info("socialSignIn - provider : {}, code : {}",provider, code);
        SignUpForm signUpForm = socialLoginService.signIn(provider, code);
        log.info("signUpForm = {}", signUpForm);
        Member member = memberService.signUp(signUpForm);
        log.info("created member :{}", member.getMemberId());

        return null;
    }

}
