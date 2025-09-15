package com.example.LMS.controller;

import com.example.LMS.dto.request.BookRequest;
import com.example.LMS.dto.response.ApiResponse;
import com.example.LMS.model.Admin;
import com.example.LMS.model.Book;
import com.example.LMS.model.BookStatus;
import com.example.LMS.repository.AdminRepository;
import com.example.LMS.repository.BookRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Management", description = "Admin management APIs")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired 
    private BookRepository bookRepository;

    @Operation(summary = "Get all admins")
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Admins retrieved successfully", admins));
    }

    @Operation(summary = "Get admin by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAdminById(@PathVariable Long id) {
        Optional<Admin> admin = adminRepository.findById(id);
        if (admin.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, "Admin retrieved successfully", admin.get()));
        }
        return ResponseEntity.notFound().build();
    }

    
    
    //books api
    @Operation(summary = "Add new book")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> addBook(@Valid @RequestBody BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        book.setDescription(request.getDescription());
        book.setImageUrl(request.getImageUrl());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getTotalCopies());
        book.setStatus(BookStatus.AVAILABLE);
        book.setIsActive(true);
        book.setCreatedDate(LocalDateTime.now());

        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(new ApiResponse(true, "Book added successfully", savedBook));
    }

    @Operation(summary = "Update admin status")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateAdminStatus(@PathVariable Long id, @RequestParam boolean isActive) {
        Optional<Admin> adminOpt = adminRepository.findById(id);
        if (!adminOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Admin admin = adminOpt.get();
        admin.setIsActive(isActive);
        adminRepository.save(admin);

        return ResponseEntity.ok(new ApiResponse(true, "Admin status updated successfully"));
    }
    
    @Operation(summary = "Update book")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (!existingBook.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Book book = existingBook.get();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        book.setDescription(request.getDescription());
        book.setImageUrl(request.getImageUrl());
        book.setTotalCopies(request.getTotalCopies());
        book.setUpdatedDate(LocalDateTime.now());

        Book updatedBook = bookRepository.save(book);
        return ResponseEntity.ok(new ApiResponse(true, "Book updated successfully", updatedBook));
    }

    @Operation(summary = "Delete book")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteBook(@PathVariable String id) {
        Optional<Book> book = bookRepository.findById(Long.parseLong(id));
        if (!book.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Book bookToDelete = book.get();
        bookToDelete.setIsActive(false);
        bookToDelete.setUpdatedDate(LocalDateTime.now());
        bookRepository.save(bookToDelete);

        return ResponseEntity.ok(new ApiResponse(true, "Book deleted successfully"));
    }
}
