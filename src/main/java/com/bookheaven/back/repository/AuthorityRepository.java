package com.bookheaven.back.repository;

import com.bookheaven.back.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByDisplayName(String displayName);
}
