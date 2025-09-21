package com.example.LMS.repository;

import com.example.LMS.model.BorrowedBook;
import com.example.LMS.model.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    
    @Query("SELECT bb FROM BorrowedBook bb WHERE bb.dueDate < :currentDate AND bb.status = :status")
    List<BorrowedBook> findOverdueBooks(@Param("currentDate") LocalDate currentDate, 
                                       @Param("status") BorrowStatus status);
    
    @Query("SELECT COUNT(bb) FROM BorrowedBook bb WHERE bb.member.id = :memberId AND bb.status = :status")
    int countByMemberIdAndStatus(@Param("memberId") Long memberId, @Param("status") BorrowStatus status);
    
    long countByBorrowDateBetween(LocalDateTime start, LocalDateTime end);
    long countByStatus(BorrowStatus status);
    
    List<BorrowedBook> findByMemberIdAndStatus(Long memberId, BorrowStatus status);

    List<BorrowedBook> findByMemberId(Long memberId);

    List<BorrowedBook> findByBookIdAndStatus(Long bookId, BorrowStatus status);

    
    @Query("SELECT SUM(bb.fine) FROM BorrowedBook bb WHERE bb.member.id = :memberId AND bb.finePaid = false")
    Double sumFineByMemberId(@Param("memberId") Long memberId);
    
    @Query("SELECT COUNT(bb) FROM BorrowedBook bb WHERE bb.dueDate < :currentDate AND bb.status = :status AND bb.returnDate IS NULL")
    long countOverdueBooks(@Param("currentDate") LocalDateTime currentDate, @Param("status") BorrowStatus status);
    
    @Query("SELECT SUM(bb.fine) FROM BorrowedBook bb WHERE bb.finePaid = false")
    Double sumAllFines();
    
    @Query("SELECT SUM(bb.fine) FROM BorrowedBook bb WHERE bb.updatedDate BETWEEN :start AND :end")
    Double sumFineByMonth(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);


}