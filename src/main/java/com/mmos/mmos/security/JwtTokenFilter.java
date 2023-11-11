package com.mmos.mmos.security;

import com.mmos.mmos.src.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;

        // Header의 Authorization 값이 비어있으면 => Jwt Token을 전송하지 않음 => 로그인 하지 않음
        // Header의 Authorization 값이 'Bearer '로 시작하지 않으면 => 잘못된 토큰
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 전송받은 값에서 'Bearer ' 뒷부분(Jwt Token) 추출
        jwt = authHeader.split(" ")[1];

        // 유저 아이디 추출
        userId = jwtService.extractUsername(jwt);

        // 유저 아이디가 존재하고 권한은 부여 받지 못했다면
        if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 유저를 식별자인 username 즉, 여기서는 로그인하려는 ID로 UserDetails 가져오기
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
            // 레포지토리에서 Jwt 찾기
            boolean isTokenValid = tokenRepository.findByToken(jwt).map(t -> !t.isExpired() && !t.isRevoked()).orElse(false);
            // 클라이언트에게 받은 Jwt가 유효한지 확인 && 서버에 저장된 토큰이 유효한지 확인
            if(jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                // 둘 다 유효하다면 권한 부여
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 다음 필터로 이동
        filterChain.doFilter(request, response);
    }
}