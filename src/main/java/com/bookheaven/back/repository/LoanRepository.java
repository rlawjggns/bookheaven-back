package com.bookheaven.back.repository;

import com.bookheaven.back.domain.Book;
import com.bookheaven.back.domain.Loan;
import com.bookheaven.back.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByBookAndMember(Book book, Member member);
}
