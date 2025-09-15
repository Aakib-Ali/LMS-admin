package com.example.LMS.controller;

import com.example.LMS.dto.request.BookRequest;
import com.example.LMS.dto.response.ApiResponse;
import com.example.LMS.model.Book;
import com.example.LMS.model.BookStatus;
import com.example.LMS.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Book management APIs")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Operation(summary = "Get all books")
    @GetMapping
    public ResponseEntity<?> getAllBooks() {
        List<Book> books = bookRepository.findByIsActiveTrue();
        System.out.println(books);
        return ResponseEntity.ok(new ApiResponse(true, "Books retrieved successfully", books));
    }

    @Operation(summary = "Get book by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent() && book.get().getIsActive()) {
            return ResponseEntity.ok(new ApiResponse(true, "Book retrieved successfully", book.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Search books")
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam String searchTerm) {
        List<Book> books = bookRepository.searchBooks(searchTerm);
        return ResponseEntity.ok(new ApiResponse(true, "Books retrieved successfully", books));
    }

    @Operation(summary = "Get books by category")
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getBooksByCategory(@PathVariable String category) {
        List<Book> books = bookRepository.findByCategoryAndIsActiveTrue(category);
        return ResponseEntity.ok(new ApiResponse(true, "Books retrieved successfully", books));
    }

    @Operation(summary = "Get all categories")
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        List<String> categories = bookRepository.findAllCategories();
        return ResponseEntity.ok(new ApiResponse(true, "Categories retrieved successfully", categories));
    }

    

    
}
