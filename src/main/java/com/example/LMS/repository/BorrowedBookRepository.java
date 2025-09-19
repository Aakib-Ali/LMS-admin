package com.example.LMS.repository;

import com.example.LMS.model.BorrowedBook;
import com.example.LMS.model.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {
    List<BorrowedBook> findByMemberIdAndStatus(Long memberId, BorrowStatus status);
    List<BorrowedBook> findByMemberId(Long memberId);
    List<BorrowedBook> findByBookIdAndStatus(Long bookId, BorrowStatus status);
    
    @Query("SELECT bb FROM BorrowedBook bb WHERE bb.dueDate < :currentDate AND bb.status = :status")
    List<BorrowedBook> findOverdueBooks(@Param("currentDate") LocalDate currentDate, 
                                       @Param("status") BorrowStatus status);
    
    @Query("SELECT COUNT(bb) FROM BorrowedBook bb WHERE bb.member.id = :memberId AND bb.status = :status")
    int countByMemberIdAndStatus(@Param("memberId") Long memberId, @Param("status") BorrowStatus status);
}