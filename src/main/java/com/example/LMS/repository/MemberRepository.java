package com.example.LMS.repository;

import com.example.LMS.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobileNumber);
    long countByRegistrationDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(m.totalFines) FROM Member m")
    Double sumOfFines();

}