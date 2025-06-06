package com.bookheaven.back.domain;

import com.bookheaven.back.dto.MemberUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = true, name = "email")
    private String email;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = true, name = "password")
    private String password;

    @Column(nullable = false, name = "tel")
    private String tel;

    @Column(nullable = false, name = "memo")
    private String memo;

    @Column(nullable = false, name = "member_role")
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @OneToMany(mappedBy = "member")
    private List<Loan> loans = new ArrayList<>();

    @Builder
    public Member(String email, String password, String name, String tel, String memo, MemberRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.tel = tel;
        this.memo = memo;
        this.role = role;
    }

    public void update(MemberUpdateRequestDto request) {
        this.name = request.getName();
        this.memo = request.getMemo();
        this.tel = request.getTel();
    }
}
