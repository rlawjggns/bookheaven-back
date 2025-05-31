package com.bookheaven.back.repository;

import com.bookheaven.back.domain.Member;
import com.bookheaven.back.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember_Email(String memberEmail);
}
