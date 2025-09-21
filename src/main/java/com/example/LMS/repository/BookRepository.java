package com.example.LMS.repository;

import com.example.LMS.dto.response.BookResponse;
import com.example.LMS.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByIsActiveTrue();
    List<Book> findByCategoryAndIsActiveTrue(String category);
    
    long countByCreatedDateBetween(LocalDateTime start, LocalDateTime end);

    
    @Query("SELECT b FROM Book b WHERE b.isActive = true  AND" +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.category) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Book> searchBooks(@Param("searchTerm") String searchTerm);
    
    
    
    List<Book> findByTitleContainingIgnoreCaseAndIsActiveTrue(String title);
    List<Book> findByAuthorContainingIgnoreCaseAndIsActiveTrue(String author);
    List<Book> findByCategoryContainingIgnoreCaseAndIsActiveTrue(String category);
    
    @Query("SELECT DISTINCT b.category FROM Book b")
    List<String> findAllCategories();
   
}