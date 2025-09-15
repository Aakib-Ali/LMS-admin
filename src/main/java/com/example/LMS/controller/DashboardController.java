package com.example.LMS.controller;

import com.example.LMS.dto.response.ApiResponse;
import com.example.LMS.model.*;
import com.example.LMS.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Dashboard statistics APIs")
public class DashboardController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private DonationRepository donationRepository;

    @Operation(summary = "Get admin dashboard statistics")
    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalBooks = bookRepository.count();
        long availableBooks = bookRepository.findByIsActiveTrue().size();
        long totalMembers = memberRepository.count();
        long totalBorrowedBooks = borrowedBookRepository.findAll().stream()
                .mapToInt(bb -> bb.getStatus() == BorrowStatus.BORROWED ? 1 : 0)
                .sum();
        long overdueBooks = borrowedBookRepository.findOverdueBooks(LocalDate.now(), BorrowStatus.BORROWED).size();
        long totalComplaints = complaintRepository.count();
        long openComplaints = complaintRepository.findByStatus(ComplaintStatus.OPEN).size();
        long totalDonations = donationRepository.count();
        long pendingDonations = donationRepository.findByStatus(DonationStatus.PENDING).size();

        stats.put("totalBooks", totalBooks);
        stats.put("availableBooks", availableBooks);
        stats.put("totalMembers", totalMembers);
        stats.put("totalBorrowedBooks", totalBorrowedBooks);
        stats.put("overdueBooks", overdueBooks);
        stats.put("totalComplaints", totalComplaints);
        stats.put("openComplaints", openComplaints);
        stats.put("totalDonations", totalDonations);
        stats.put("pendingDonations", pendingDonations);

        return ResponseEntity.ok(new ApiResponse(true, "Dashboard stats retrieved successfully", stats));
    }

    @Operation(summary = "Get member dashboard statistics")
    @GetMapping("/member/{memberId}/stats")
    public ResponseEntity<?> getMemberDashboardStats(@PathVariable Long memberId) {
        Map<String, Object> stats = new HashMap<>();
        
        int currentBorrowedBooks = borrowedBookRepository.countByMemberIdAndStatus(memberId, BorrowStatus.BORROWED);
        long totalBorrowHistory = borrowedBookRepository.findByMemberId(memberId).size();
        long totalComplaints = complaintRepository.findByMemberId(memberId).size();
        long openComplaints = complaintRepository.findByMemberIdAndStatus(memberId, ComplaintStatus.OPEN).size();
        long totalDonations = donationRepository.findByMemberId(memberId).size();
        long pendingDonations = donationRepository.findByMemberIdAndStatus(memberId, DonationStatus.PENDING).size();

        stats.put("currentBorrowedBooks", currentBorrowedBooks);
        stats.put("totalBorrowHistory", totalBorrowHistory);
        stats.put("totalComplaints", totalComplaints);
        stats.put("openComplaints", openComplaints);
        stats.put("totalDonations", totalDonations);
        stats.put("pendingDonations", pendingDonations);

        return ResponseEntity.ok(new ApiResponse(true, "Member dashboard stats retrieved successfully", stats));
    }
}
