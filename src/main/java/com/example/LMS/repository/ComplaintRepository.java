package com.example.LMS.repository;

import com.example.LMS.model.Complaint;
import com.example.LMS.model.ComplaintStatus;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByMemberId(Long memberId);
    List<Complaint> findByMemberIdAndStatus(Long memberId, ComplaintStatus status);
    Page<Complaint> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "responses")
    Page<Complaint> findByStatus(ComplaintStatus status, Pageable pageable);
    
    long countByStatus(ComplaintStatus status);
    long countByStatusAndResolutionDateBetween(ComplaintStatus status, LocalDateTime start, LocalDateTime end);

    long countBySubmissionDateBetween(LocalDateTime start, LocalDateTime end);
}
