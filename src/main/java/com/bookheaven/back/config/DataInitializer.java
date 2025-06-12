package com.bookheaven.back.config;

import com.bookheaven.back.domain.*;
import com.bookheaven.back.repository.BookRepository;
import com.bookheaven.back.repository.LoanRepository;
import com.bookheaven.back.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 초기 데이터 삽입용 클래스
 */
@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(MemberRepository memberRepository, BookRepository bookRepository, LoanRepository loanRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 관리자 계정
        if (memberRepository.findByEmail("admin@example.com").isEmpty()) {
            Member admin = Member.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin1234"))
                    .name("관리자")
                    .tel("010-1234-5678")
                    .memo("시스템 관리용 계정입니다.")
                    .role(MemberRole.ADMIN)
                    .build();
            memberRepository.save(admin);
            log.info("✅ 관리자 계정 생성: admin@example.com");
        }

        // 일반 사용자 계정들
        for (int i = 1; i <= 5; i++) {
            String email = "user" + i + "@example.com";
            if (memberRepository.findByEmail(email).isEmpty()) {
                Member user = Member.builder()
                        .email(email)
                        .password(passwordEncoder.encode("user1234"))
                        .name("사용자" + i)
                        .tel("010-0000-000" + i)
                        .memo("일반 사용자 계정입니다.")
                        .role(MemberRole.REGULAR)
                        .build();
                memberRepository.save(user);
                log.info("일반 사용자 계정 생성: {}", email);
            }
        }

        // 도서 데이터
        if (bookRepository.count() == 0) {
            bookRepository.saveAll(List.of(
                    Book.builder().title("어린왕자").author("생텍쥐페리").publisher("열린책들").publicationYear(1943).price(12000).build(),
                    Book.builder().title("데미안").author("헤르만 헤세").publisher("민음사").publicationYear(1919).price(13000).build(),
                    Book.builder().title("1984").author("조지 오웰").publisher("문학동네").publicationYear(1949).price(15000).build(),
                    Book.builder().title("죄와 벌").author("도스토예프스키").publisher("창비").publicationYear(1866).price(18000).build(),
                    Book.builder().title("이방인").author("알베르 카뮈").publisher("책세상").publicationYear(1942).price(14000).build()
            ));
            log.info("도서 5권 데이터 삽입 완료");
        }

        // 대출 데이터
        if (loanRepository.count() == 0) {
            List<Member> users = memberRepository.findAll();
            List<Book> books = bookRepository.findAll();

            if (users.size() >= 3 && books.size() >= 3) {
                loanRepository.saveAll(List.of(
                        // user1 대출 - 반납 완료
                        Loan.builder()
                                .member(users.get(1))
                                .book(books.get(0))
                                .loanDate(LocalDate.now().minusDays(10))
                                .returnDate(LocalDate.now().minusDays(2))
                                .returnStatus(true)
                                .build(),
                        // user2 대출 - 반납 안함, 연체 아님
                        Loan.builder()
                                .member(users.get(2))
                                .book(books.get(1))
                                .loanDate(LocalDate.now().minusDays(3))
                                .returnDate(LocalDate.now().plusDays(4))
                                .returnStatus(false)
                                .build(),
                        // user3 대출 - 반납 안함, 연체 중
                        Loan.builder()
                                .member(users.get(3))
                                .book(books.get(2))
                                .loanDate(LocalDate.now().minusDays(15))
                                .returnDate(LocalDate.now().minusDays(5))
                                .returnStatus(false)
                                .build()
                ));
                log.info("대출 데이터 3건 삽입 완료");
            }
        }
    }
}
