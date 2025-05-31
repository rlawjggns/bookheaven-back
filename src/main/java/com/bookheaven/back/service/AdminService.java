package com.bookheaven.back.service;

import com.bookheaven.back.domain.Book;
import com.bookheaven.back.domain.Loan;
import com.bookheaven.back.domain.Member;
import com.bookheaven.back.domain.MemberRole;
import com.bookheaven.back.dto.*;
import com.bookheaven.back.repository.BookRepository;
import com.bookheaven.back.repository.LoanRepository;
import com.bookheaven.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    public Long createMember(MemberCreateRequestDto request) {
        Member member = Member.builder()
                .name(request.getName())
                .tel(request.getTel())
                .memo(request.getMemo())
                .role(MemberRole.REGULAR)
                .build();

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    public Long updateMember(MemberUpdateRequestDto request) {
        Member member = memberRepository.findById(request.getId()).orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다."));
        member.update(request);

        return member.getId();
    }

    public List<LoanResponseDto> getMemberLoans(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다."));
        return Optional.ofNullable(member.getLoans())
                .orElse(Collections.emptyList())
                .stream()
                .map(Loan::toDto)
                .toList();
    }

    public Long createBook(BookCreateRequestDto request) {
        Book book = Book.builder()
                .title(request.getTitle())
                .price(request.getPrice())
                .author(request.getAuthor())
                .publicationYear(request.getPublicationYear())
                .publisher(request.getPublisher())
                .build();

        Book savedBook = bookRepository.save(book);
        return savedBook.getId();
    }

    public Long updateBook(BookUpdateRequestDto request) {
        Book book = bookRepository.findById(request.getId()).orElseThrow(() -> new NoSuchElementException("해당 도서를 찾을 수 없습니다."));
        book.update(request);

        return book.getId();
    }

    public List<LoanResponseDto> getBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NoSuchElementException("해당 도서를 찾을 수 없습니다."));

        return Optional.ofNullable(book.getLoans())
                .orElse(Collections.emptyList())
                .stream()
                .map(Loan::toDto)
                .toList();
    }

    public String loanBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NoSuchElementException("해당 도서를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다."));

        Loan loan = Loan.builder()
                .book(book)
                .member(member)
                .loanDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(15))
                .returnStatus(Boolean.FALSE)
                .build();

        Loan savedLoan = loanRepository.save(loan);
        return "성공적으로 대출이 진행되었습니다.";
    }

    public String returnBook(Long bookId, Long memberId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NoSuchElementException("해당 도서를 찾을 수 없습니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다."));


        loanRepository.findByBookAndMember(book, member)
                .forEach(Loan::returnBook);
        return "성공적으로 반납이 진행되었습니다.";
    }
}
