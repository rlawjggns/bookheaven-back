package com.bookheaven.back.security;

import com.bookheaven.back.domain.Member;
import com.bookheaven.back.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("이메일을 찾을 수 없습니다: " + email));
    }

    private UserDetails createUserDetails(Member member) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + member.getRole());
        return new User(
                member.getEmail(),
                member.getPassword(),
                Collections.singleton(authority)
        );
    }
}
