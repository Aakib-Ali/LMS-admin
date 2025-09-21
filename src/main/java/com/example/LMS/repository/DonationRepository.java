package com.example.LMS.repository;

import com.example.LMS.model.Donation;
import com.example.LMS.model.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByMemberId(Long memberId);
    List<Donation> findByStatus(DonationStatus status);
    List<Donation> findByMemberIdAndStatus(Long memberId, DonationStatus status);
    long countByStatus(DonationStatus status);

    
}
