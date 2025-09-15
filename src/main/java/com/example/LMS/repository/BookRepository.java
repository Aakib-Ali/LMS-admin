package com.example.LMS.repository;

import com.example.LMS.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByIsActiveTrue();
    List<Book> findByCategoryAndIsActiveTrue(String category);
    
    @Query("SELECT b FROM Book b WHERE " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "b.isActive = true")
    List<Book> searchBooks(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT DISTINCT b.category FROM Book b WHERE b.isActive = true")
    List<String> findAllCategories();
}
