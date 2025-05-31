package com.bookheaven.back.controller;

import com.bookheaven.back.domain.Member;
import com.bookheaven.back.dto.LoginRequestDto;
import com.bookheaven.back.dto.TokenDto;
import com.bookheaven.back.repository.MemberRepository;
import com.bookheaven.back.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AuthController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenDto> signin(@RequestParam("username") String email,
                                           @RequestParam("password") String password) {

        return memberRepository.findByEmail(email)
                .filter(member -> passwordEncoder.matches(password, member.getPassword()))
                .map(member -> {
                    String accessToken = jwtTokenProvider.createTokenFromMember(member);
                    String refreshToken = jwtTokenProvider.createRefreshTokenFromMember(member);

                    TokenDto tokenDto = TokenDto.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .email(member.getEmail())
                            .build();

                    return ResponseEntity.ok(tokenDto);
                })
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
    }

    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuth() {
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // JWT는 서버에 상태를 저장하지 않으므로 클라이언트 측에서 토큰을 제거합니다.
        // 실제 토큰 무효화는 클라이언트에서 처리됩니다.
        return ResponseEntity.ok().body("로그아웃 성공");
    }
}
