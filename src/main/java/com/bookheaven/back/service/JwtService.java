package com.bookheaven.back.service;

import com.bookheaven.back.configuration.JwtProperties;
import com.bookheaven.back.domain.Member;
import com.bookheaven.back.domain.RefreshToken;
import com.bookheaven.back.dto.ResponseAccessToken;
import com.bookheaven.back.repository.MemberRepository;
import com.bookheaven.back.repository.RefreshTokenRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
public class JwtService {
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;
    private final SecretKey refreshSecretKey;
    private final JwtParser jwtParser;
    private final JwtParser refreshJwtParser;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    private final MemberRepository memberRepository;

    public JwtService(JwtProperties jwtProperties, UserDetailsService userDetailsService, RefreshTokenRepository refreshTokenRepository, MemberRepository memberRepository) {
        this.jwtProperties = jwtProperties;
        this.userDetailsService = userDetailsService;
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProperties.getSecretKey()));
        refreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProperties.getRefreshKey()));
        jwtParser = Jwts.parser().verifyWith(secretKey).build();
        refreshJwtParser = Jwts.parser().verifyWith(refreshSecretKey).build();
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
    }

    public String generateToken(String username, String type) {
        if(type==null || !type.equals(REFRESH_TOKEN)){ type=ACCESS_TOKEN; }

        Date now = new Date();
        Duration duration = Duration.ofMinutes( type.equals(ACCESS_TOKEN)? jwtProperties.getDuration(): jwtProperties.getRefreshDuration());
        Date expiration = new Date(now.getTime() + duration.toMillis());

        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(username)
                .expiration(expiration)
                .signWith(type.equals(ACCESS_TOKEN)? secretKey:refreshSecretKey)
                .compact();
    }

    // 적합한 사용자가 아니면 Exception 발생
    public Authentication verifyToken(String token) throws JwtException, UsernameNotFoundException {
        String username = jwtParser.parseSignedClaims(token).getPayload().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }

    @Transactional
    public ResponseAccessToken getAccessTokenByUsername(Member member) {
        String accessToken = generateToken(member.getEmail(), ACCESS_TOKEN);
        String refreshToken = generateToken(member.getEmail(), REFRESH_TOKEN);
        // RT 저장
        RefreshToken rtEntity = refreshTokenRepository.findByMember_Email(member.getEmail()).orElse(null);
        if(rtEntity == null){
            rtEntity = new RefreshToken();
            rtEntity.setMember(member);
        }
        rtEntity.setRefreshToken(refreshToken);
        refreshTokenRepository.save(rtEntity);

        return ResponseAccessToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public ResponseAccessToken getAccessTokenByRefreshToken(String token) throws JwtException, UsernameNotFoundException {
        String username = refreshJwtParser.parseSignedClaims(token).getPayload().getSubject();
        Member member = memberRepository.findByEmail(username).orElse(null);
        RefreshToken refreshToken = refreshTokenRepository.findByMember_Email(username).orElse(null);

        if(member == null) {
            return ResponseAccessToken.builder().error("Unknown member.").build();
        }

        if(refreshToken != null && ! refreshToken.getRefreshToken().equals(token)){
            refreshTokenRepository.delete(refreshToken); // 비정상 로그인 시도로 봐서 RT 삭제.
            return ResponseAccessToken.builder().error("Refresh Failed").build();
        }

        return getAccessTokenByUsername(member);
    }
}
