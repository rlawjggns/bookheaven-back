package com.bookheaven.back.service;

import com.bookheaven.back.repository.BookRepository;
import com.bookheaven.back.repository.LoanRepository;
import com.bookheaven.back.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;
}
