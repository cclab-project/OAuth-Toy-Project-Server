package com.cclab.oauth.domain.jwt.filter;

import com.cclab.oauth.domain.jwt.service.TokenService;
import com.cclab.oauth.security.UserDetailsImpl;
import com.cclab.oauth.utils.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("jwtFilter집입");

        String username = null;
        String jwt = null;

        Optional<Cookie> jwtCookie = CookieUtils.getCookie(request, "access_token");

        /*if(jwtCookie.isEmpty()){
            filterChain.doFilter(request, response);
            jwt = jwtCookie.get().getValue();
            username = tokenService.validationTokenWithUserEmail(jwt);
        }else {
            filterChain.doFilter(request, response);
        }*/


        /*
         * 토큰에서 username 을 정상적으로 추출할 수 있고
         * SecurityContextHolder 내에 authentication 객체(이전에 인증된 정보)가 없는 상태인지를 검사한다.
         *
         * */


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);

            //토큰이 유효하다면
            if (tokenService.validationToken(jwt, userDetails.getUsername())) {
                //새로운 인증 정보를 생성
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //인증 정보를 SecurityContextHolder 에 저장
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
