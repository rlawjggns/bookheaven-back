package com.bookheaven.back.domain;

import com.bookheaven.back.dto.LoanResponseDto;
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

    @Column(nullable = false, name = "loan_date")
    private LocalDate loanDate;

    @Column(nullable = false, name = "return_date")
    private LocalDate returnDate;

    @Column(nullable = false, name = "return_status")
    private Boolean returnStatus;

    @Column(nullable = false, name = "overdue_status")
    private Boolean overdueStatus = Boolean.FALSE;

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

    public LoanResponseDto toDto() {

        Boolean currentOverdueStatus;
        if (!this.returnStatus) { // 아직 반납 전
            currentOverdueStatus = LocalDate.now().isAfter(this.getReturnDate());
        } else { // 반납 후
            currentOverdueStatus = this.getOverdueStatus();
        }

        return LoanResponseDto.builder()
                .memberName(member.getName())
                .memberId(member.getId())
                .bookId(book.getId())
                .bookName(book.getTitle())
                .bookAuthor(book.getAuthor())
                .bookPublisher(book.getPublisher())
                .loanDate(this.getLoanDate())
                .returnDate(this.getReturnDate())
                .returnStatus(this.getReturnStatus())
                .overdueStatus(currentOverdueStatus)
                .build();
    }

    public void returnBook() {
        boolean isOverdue = LocalDate.now().isAfter(this.getReturnDate());
        this.overdueStatus = isOverdue;
        this.returnStatus = Boolean.TRUE;
    }
}
