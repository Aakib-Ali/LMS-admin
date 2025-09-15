package com.example.LMS.controller;

import com.example.LMS.dto.request.BorrowBookRequest;
import com.example.LMS.dto.response.ApiResponse;
import com.example.LMS.model.*;
import com.example.LMS.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/borrowed-books")
@Tag(name = "Borrowed Books", description = "Book borrowing management APIs")
public class BorrowedBookController {

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Operation(summary = "Get all borrowed books")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAllBorrowedBooks() {
        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Borrowed books retrieved successfully", borrowedBooks));
    }

    @Operation(summary = "Get borrowed books by member")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getBorrowedBooksByMember(@PathVariable Long memberId) {
        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByMemberId(memberId);
        return ResponseEntity.ok(new ApiResponse(true, "Borrowed books retrieved successfully", borrowedBooks));
    }

    @Operation(summary = "Get active borrowed books by member")
    @GetMapping("/member/{memberId}/active")
    public ResponseEntity<?> getActiveBorrowedBooksByMember(@PathVariable Long memberId) {
        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findByMemberIdAndStatus(memberId, BorrowStatus.BORROWED);
        return ResponseEntity.ok(new ApiResponse(true, "Active borrowed books retrieved successfully", borrowedBooks));
    }

    @Operation(summary = "Get overdue books")
    @GetMapping("/overdue")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getOverdueBooks() {
        List<BorrowedBook> overdueBooks = borrowedBookRepository.findOverdueBooks(LocalDate.now(), BorrowStatus.BORROWED);
        return ResponseEntity.ok(new ApiResponse(true, "Overdue books retrieved successfully", overdueBooks));
    }

    @Operation(summary = "Borrow a book")
    @PostMapping("/borrow")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<?> borrowBook(@Valid @RequestBody BorrowBookRequest request) {
        Optional<Book> bookOpt = bookRepository.findById(request.getBookId());
        Optional<Member> memberOpt = memberRepository.findById(request.getMemberId());

        if (!bookOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Book not found"));
        }

        if (!memberOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Member not found"));
        }

        Book book = bookOpt.get();
        Member member = memberOpt.get();

        if (book.getAvailableCopies() <= 0) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Book not available"));
        }

        int currentBorrowedCount = borrowedBookRepository.countByMemberIdAndStatus(member.getId(), BorrowStatus.BORROWED);
        if (currentBorrowedCount >= 5) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Member has reached borrowing limit"));
        }

        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBook(book);
        borrowedBook.setMember(member);
        borrowedBook.setBorrowDate(LocalDate.now());
        borrowedBook.setDueDate(LocalDate.now().plusDays(14));
        borrowedBook.setStatus(BorrowStatus.BORROWED);
        borrowedBook.setFine(0.0);
        borrowedBook.setCreatedDate(LocalDateTime.now());

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        if (book.getAvailableCopies() == 0) {
            book.setStatus(BookStatus.BORROWED);
        }

        member.setCurrentBorrowedBooks(member.getCurrentBorrowedBooks() + 1);

        borrowedBookRepository.save(borrowedBook);
        bookRepository.save(book);
        memberRepository.save(member);

        return ResponseEntity.ok(new ApiResponse(true, "Book borrowed successfully", borrowedBook));
    }

    @Operation(summary = "Return a book")
    @PostMapping("/return/{borrowedBookId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> returnBook(@PathVariable Long borrowedBookId) {
        Optional<BorrowedBook> borrowedBookOpt = borrowedBookRepository.findById(borrowedBookId);

        if (!borrowedBookOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Borrowed book record not found"));
        }

        BorrowedBook borrowedBook = borrowedBookOpt.get();

        if (borrowedBook.getStatus() != BorrowStatus.BORROWED) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Book already returned"));
        }

        borrowedBook.setReturnDate(LocalDate.now());
        borrowedBook.setStatus(BorrowStatus.RETURNED);
        borrowedBook.setUpdatedDate(LocalDateTime.now());

        if (LocalDate.now().isAfter(borrowedBook.getDueDate())) {
            long overdueDays = LocalDate.now().toEpochDay() - borrowedBook.getDueDate().toEpochDay();
            double fine = overdueDays * 1.0;
            borrowedBook.setFine(fine);
        }

        Book book = borrowedBook.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        book.setStatus(BookStatus.AVAILABLE);

        Member member = borrowedBook.getMember();
        member.setCurrentBorrowedBooks(member.getCurrentBorrowedBooks() - 1);
        if (borrowedBook.getFine() > 0) {
            member.setTotalFines(member.getTotalFines() + borrowedBook.getFine());
        }

        borrowedBookRepository.save(borrowedBook);
        bookRepository.save(book);
        memberRepository.save(member);

        return ResponseEntity.ok(new ApiResponse(true, "Book returned successfully", borrowedBook));
    }

    @Operation(summary = "Renew a borrowed book")
    @PostMapping("/renew/{borrowedBookId}")
    public ResponseEntity<?> renewBook(@PathVariable Long borrowedBookId) {
        Optional<BorrowedBook> borrowedBookOpt = borrowedBookRepository.findById(borrowedBookId);

        if (!borrowedBookOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Borrowed book record not found"));
        }

        BorrowedBook borrowedBook = borrowedBookOpt.get();

        if (borrowedBook.getStatus() != BorrowStatus.BORROWED) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Book is not currently borrowed"));
        }

        if (borrowedBook.getRenewalCount() >= 2) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Maximum renewal limit reached"));
        }

        borrowedBook.setDueDate(borrowedBook.getDueDate().plusDays(14));
        borrowedBook.setRenewalCount(borrowedBook.getRenewalCount() + 1);
        borrowedBook.setUpdatedDate(LocalDateTime.now());

        borrowedBookRepository.save(borrowedBook);

        return ResponseEntity.ok(new ApiResponse(true, "Book renewed successfully", borrowedBook));
    }
}
