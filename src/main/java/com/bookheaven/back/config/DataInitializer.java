package com.bookheaven.back.config;

import com.bookheaven.back.domain.Member;
import com.bookheaven.back.domain.MemberRole;
import com.bookheaven.back.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 초기 관리자 계정 생성
        if (memberRepository.findByEmail("admin@bookheaven.com").isEmpty()) {
            Member admin = Member.builder()
                    .email("admin@bookheaven.com")
                    .password(passwordEncoder.encode("admin1234"))
                    .tel("010-1234-5678")
                    .memo("시스템 관리자")
                    .role(MemberRole.ADMIN)
                    .build();

            memberRepository.save(admin);
            System.out.println("관리자 계정이 생성되었습니다. (admin@bookheaven.com)");
        }
    }
}
