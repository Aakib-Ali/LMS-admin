package com.example.LMS.controller;

import com.example.LMS.dto.request.BookRequest;
import com.example.LMS.dto.response.ApiResponse;
import com.example.LMS.dto.response.BookResponse;
import com.example.LMS.model.Book;
import com.example.LMS.model.BookStatus;
import com.example.LMS.repository.BookRepository;
import com.example.LMS.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Book management APIs")
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private BookService bookService;

    @Operation(summary = "Get all books")
    @GetMapping
    public ResponseEntity<?> getAllBooks() {
        List<BookResponse> books =bookService.getAllActiveBooks();
        return ResponseEntity.ok(new ApiResponse(true, "Books retrieved successfully", books));
    }

  //books api
    

    
    @Operation(summary = "Get book by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable String id) {
        Optional<Book> book = bookRepository.findById(Long.parseLong(id));
        if (book.isPresent() && book.get().getIsActive()) {
            return ResponseEntity.ok(new ApiResponse(true, "Book retrieved successfully", book.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Search books")
    @GetMapping("/search")
    public ResponseEntity<?> searchBooks(@RequestParam String searchTerm, @RequestParam(defaultValue="all") String searchType) {
        List<BookResponse> books = bookService.searchBooks(searchTerm,searchType);
        return ResponseEntity.ok(new ApiResponse(true, "Books retrieved successfully", books));
    }

    @Operation(summary = "Get books by category")
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getBooksByCategory(@PathVariable String category) {
        List<Book> books = bookRepository.findByCategoryContainingIgnoreCaseAndIsActiveTrue(category);
        return ResponseEntity.ok(new ApiResponse(true, "Books retrieved successfully", books));
    }

    @Operation(summary = "Get all categories")
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        List<String> categories = bookRepository.findAllCategories();
        return ResponseEntity.ok(new ApiResponse(true, "Categories retrieved successfully", categories));
    }

    
}