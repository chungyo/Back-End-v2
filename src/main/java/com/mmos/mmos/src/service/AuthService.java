package com.mmos.mmos.src.service;

import com.mmos.mmos.config.exception.BaseException;
import com.mmos.mmos.security.JwtService;
import com.mmos.mmos.src.domain.dto.request.LoginRequestDto;
import com.mmos.mmos.src.domain.entity.TokenType;
import com.mmos.mmos.src.domain.entity.Tokens;
import com.mmos.mmos.src.domain.entity.User;
import com.mmos.mmos.src.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mmos.mmos.config.HttpResponseStatus.DATABASE_ERROR;
import static com.mmos.mmos.config.HttpResponseStatus.LOGIN_FAIL;
import static com.mmos.mmos.utils.SHA256.encrypt;

@Service
@RequiredArgsConstructor
public class AuthService implements LogoutHandler {

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

    public String authenticate(LoginRequestDto requestDto) throws BaseException {
        try {
            User user = userService.findUserById(requestDto.getId());

            // 암호화 된 비밀번호와 이미 암호화된 비밀번호를 비교
            if(!user.getUserPassword().equals(encrypt(requestDto.getPwd())))
                throw new BaseException(LOGIN_FAIL);

            String jwtToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveToken(user, jwtToken);
            return jwtToken;
        } catch (BaseException e) {
            throw new BaseException(e.getStatus());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void revokeAllUserTokens(User user) {
        List<Tokens> validTokens = tokenRepository.findAllByUserNameAndExpiredIsFalseAndRevokedIsFalse(user.getUserId());
        System.out.println("validTokens = " + validTokens);
        if (!validTokens.isEmpty()) {
            validTokens.forEach( t-> {
                t.setExpired(true);
                t.setRevoked(true);
            });
            tokenRepository.saveAll(validTokens);
        }
    }

    private void saveToken (User user, String jwtToken) {
        Tokens token = Tokens.builder()
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .userName(user.getUserId())
                .build();
        tokenRepository.save(token);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        jwt = authHeader.substring(7);
        Tokens storedToken = tokenRepository.findByToken(jwt).orElse(null);
        if(storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }
    }
}
