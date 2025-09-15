package com.example.LMS.repository;

import com.example.LMS.model.Complaint;
import com.example.LMS.model.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByMemberId(Long memberId);
    List<Complaint> findByStatus(ComplaintStatus status);
    List<Complaint> findByMemberIdAndStatus(Long memberId, ComplaintStatus status);
}
