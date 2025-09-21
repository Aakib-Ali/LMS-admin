package com.example.LMS.service;

import com.example.LMS.model.*;
import com.example.LMS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowingService {

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    private static final int MAX_BOOKS_PER_MEMBER = 5;
    private static final int LENDING_PERIOD_DAYS = 14;
    private static final double FINE_PER_DAY = 1.0;
    private static final int MAX_RENEWALS = 2;

    @Transactional
    public BorrowedBook borrowBook(Long bookId, Long memberId, String issuedBy) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        Optional<Member> memberOpt = memberRepository.findById(memberId);

        if (!bookOpt.isPresent() || !memberOpt.isPresent()) {
            throw new RuntimeException("Book or Member not found");
        }

        Book book = bookOpt.get();
        Member member = memberOpt.get();

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Book not available");
        }

        int currentBorrowedCount = borrowedBookRepository.countByMemberIdAndStatus(memberId, BorrowStatus.BORROWED);
        if (currentBorrowedCount >= MAX_BOOKS_PER_MEMBER) {
            throw new RuntimeException("Member has reached borrowing limit");
        }

        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBook(book);
        borrowedBook.setMember(member);
        borrowedBook.setBorrowDate(LocalDateTime.now());
        borrowedBook.setDueDate(LocalDateTime.now().plusDays(LENDING_PERIOD_DAYS));
        borrowedBook.setStatus(BorrowStatus.BORROWED);
        borrowedBook.setFine(0.0);
        borrowedBook.setIssuedBy(issuedBy);
        borrowedBook.setCreatedDate(LocalDateTime.now());

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        if (book.getAvailableCopies() == 0) {
            book.setStatus(BookStatus.BORROWED);
        }

        member.setCurrentBorrowedBooks(member.getCurrentBorrowedBooks() + 1);

        borrowedBookRepository.save(borrowedBook);
        bookRepository.save(book);
        memberRepository.save(member);

        return borrowedBook;
    }

    @Transactional
    public BorrowedBook returnBook(Long borrowedBookId, String returnedTo) {
        Optional<BorrowedBook> borrowedBookOpt = borrowedBookRepository.findById(borrowedBookId);

        if (!borrowedBookOpt.isPresent()) {
            throw new RuntimeException("Borrowed book record not found");
        }

        BorrowedBook borrowedBook = borrowedBookOpt.get();

        if (borrowedBook.getStatus() != BorrowStatus.BORROWED) {
            throw new RuntimeException("Book already returned");
        }

        borrowedBook.setReturnDate(LocalDateTime.now());
        borrowedBook.setStatus(BorrowStatus.RETURNED);
        borrowedBook.setReturnedTo(returnedTo);
        borrowedBook.setUpdatedDate(LocalDateTime.now());

        if (LocalDate.now().isAfter(borrowedBook.getDueDate().toLocalDate())) {
            long overdueDays = LocalDate.now().toEpochDay() - borrowedBook.getDueDate().toLocalDate().toEpochDay();
            double fine = overdueDays * FINE_PER_DAY;
            borrowedBook.setFine(fine);
            borrowedBook.setStatus(BorrowStatus.OVERDUE);
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

        return borrowedBook;
    }

    @Transactional
    public BorrowedBook renewBook(Long borrowedBookId) {
        Optional<BorrowedBook> borrowedBookOpt = borrowedBookRepository.findById(borrowedBookId);

        if (!borrowedBookOpt.isPresent()) {
            throw new RuntimeException("Borrowed book record not found");
        }

        BorrowedBook borrowedBook = borrowedBookOpt.get();

        if (borrowedBook.getStatus() != BorrowStatus.BORROWED) {
            throw new RuntimeException("Book is not currently borrowed");
        }

        if (borrowedBook.getRenewalCount() >= MAX_RENEWALS) {
            throw new RuntimeException("Maximum renewal limit reached");
        }

        borrowedBook.setDueDate(borrowedBook.getDueDate().plusDays(LENDING_PERIOD_DAYS));
        borrowedBook.setRenewalCount(borrowedBook.getRenewalCount() + 1);
        borrowedBook.setStatus(BorrowStatus.RENEWED);
        borrowedBook.setUpdatedDate(LocalDateTime.now());

        return borrowedBookRepository.save(borrowedBook);
    }

    public List<BorrowedBook> getOverdueBooks() {
        return borrowedBookRepository.findOverdueBooks(LocalDate.now(), BorrowStatus.BORROWED);
    }

    public List<BorrowedBook> getMemberBorrowHistory(Long memberId) {
        return borrowedBookRepository.findByMemberId(memberId);
    }

    public List<BorrowedBook> getMemberActiveBorrowedBooks(Long memberId) {
        return borrowedBookRepository.findByMemberIdAndStatus(memberId, BorrowStatus.BORROWED);
    }
}