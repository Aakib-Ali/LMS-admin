package com.example.LMS.service;

import com.example.LMS.model.Book;
import com.example.LMS.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllActiveBooks() {
        return bookRepository.findByIsActiveTrue();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> searchBooks(String searchTerm) {
        return bookRepository.searchBooks(searchTerm);
    }

    public List<Book> getBooksByCategory(String category) {
        return bookRepository.findByCategoryAndIsActiveTrue(category);
    }

    public List<String> getAllCategories() {
        return bookRepository.findAllCategories();
    }

    public Book saveBook(Book book) {
        if (book.getId() == null) {
            book.setCreatedDate(LocalDateTime.now());
        } else {
            book.setUpdatedDate(LocalDateTime.now());
        }
        return bookRepository.save(book);
    }

    public void deactivateBook(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            Book bookToDeactivate = book.get();
            bookToDeactivate.setIsActive(false);
            bookToDeactivate.setUpdatedDate(LocalDateTime.now());
            bookRepository.save(bookToDeactivate);
        }
    }

    public boolean isBookAvailable(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        return book.isPresent() && book.get().getAvailableCopies() > 0 && book.get().getIsActive();
    }
}
