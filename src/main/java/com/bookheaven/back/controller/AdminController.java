package com.bookheaven.back.controller;

import com.bookheaven.back.dto.*;
import com.bookheaven.back.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    /**
     * 사용자 등록(일련번호, 이름, 전화번호, 메모(부서나 기타 정보 등록) 등으로 등록)
     */
    @PostMapping("/members")
    public ResponseEntity<?> createMember(@RequestBody MemberCreateRequestDto request) {
        Long memberId = adminService.createMember(request);
        return ResponseEntity.ok().body("id = " + memberId + " 사용자가 성공적으로 등록되었습니다.");
    }

    /**
     * 사용자별 대출현황 조회(연체 여부도 표시)
     */
    @GetMapping("/members")
    public ResponseEntity<List<LoanResponseDto>> getMemberLoans(@RequestParam("memberId") Long memberId) {
        return ResponseEntity.ok().body(adminService.getMemberLoans(memberId));
    }

    /**
     * 사용자 수정(일련번호, 이름, 전화번호, 메모(부서나 기타 정보 등록) 등으로 수정)
     */
    @PatchMapping("/members")
    public ResponseEntity<String> updateMember(@RequestBody MemberUpdateRequestDto request) {
        Long memberId = adminService.updateMember(request);
        return ResponseEntity.ok().body("id = " + memberId + " 사용자가 성공적으로 수정되었습니다.");
    }

    /**
     * 책 등록
     */
    @PostMapping("/books")
    public ResponseEntity<String> createBook(@RequestBody BookCreateRequestDto request) {
        Long bookId = adminService.createBook(request);
        return ResponseEntity.ok().body("id = " + bookId + " 도서가 성공적으로 등록되었습니다.");
    }

    /**
     * 책 조회 (대출 현황 및 이력)
     */
    @GetMapping("/books")
    public ResponseEntity<List<LoanResponseDto>> getBook(@RequestParam("bookId") Long bookId) {
        return ResponseEntity.ok().body(adminService.getBook(bookId));
    }

    /**
     * 책 수정
     */
    @PatchMapping("/books")
    public ResponseEntity<String> updateBook(@RequestBody BookUpdateRequestDto request) {
        Long bookId = adminService.updateBook(request);
        return ResponseEntity.ok().body("id = " + bookId + " 도서가 성공적으로 수정되었습니다.");
    }

    /**
     * 책 대출 (도서 등록 번호와 사용자 번호로 대출 처리, 대출기간은 15일)
     */
    @PostMapping("/books/loan")
    public ResponseEntity<String> loanBook(@RequestParam("bookId") Long bookId,
                                           @RequestParam("memberId") Long memberId) {
        return ResponseEntity.ok(adminService.loanBook(bookId, memberId));
    }

    /**
     * 책 반납 (도서 등록 번호와 사용자 번호로 반납 처리)
     */
    @PostMapping("/books/return")
    public ResponseEntity<String> returnBook(@RequestParam("bookId") Long bookId,
                                             @RequestParam("memberId") Long memberId) {
        return ResponseEntity.ok(adminService.returnBook(bookId, memberId));
    }
}
