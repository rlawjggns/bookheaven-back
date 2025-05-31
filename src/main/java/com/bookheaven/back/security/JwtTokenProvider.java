package com.bookheaven.back.security;

import com.bookheaven.back.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final Key refreshKey;
    private final long tokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private final String issuer;

    public JwtTokenProvider(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.refresh-key}") String refreshKey,
            @Value("${jwt.duration}") long tokenValidityInHours,
            @Value("${jwt.refresh-duration}") long refreshTokenValidityInDays,
            @Value("${jwt.issuer}") String issuer) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        byte[] refreshKeyBytes = Decoders.BASE64.decode(refreshKey);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
        this.tokenValidityInMilliseconds = tokenValidityInHours * 60 * 60 * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInDays * 24 * 60 * 60 * 1000;
        this.issuer = issuer;
    }

    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setIssuer(issuer)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createTokenFromMember(Member member) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(member.getEmail())
                .claim("auth", "ROLE_" + member.getRole())
                .claim("id", member.getId())
                .setIssuer(issuer)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuer(issuer)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshTokenFromMember(Member member) {
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(member.getEmail())
                .setIssuer(issuer)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities;
        Object authClaim = claims.get("auth");

        if (authClaim != null) {
            authorities = Arrays.stream(authClaim.toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        } else {
            // 기본 권한 부여
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(refreshKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromRefreshToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(refreshKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
