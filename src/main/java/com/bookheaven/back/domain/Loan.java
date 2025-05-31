package com.bookheaven.back.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "loan_date")
    private LocalDate loanDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "return_status")
    private Boolean returnStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @Builder
    public Loan(LocalDate loanDate, LocalDate returnDate, Boolean returnStatus, Member member, Book book) {
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.returnStatus = returnStatus;
        this.member = member;
        this.book = book;
    }
}
