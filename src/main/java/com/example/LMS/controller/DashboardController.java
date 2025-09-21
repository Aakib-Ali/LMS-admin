package com.example.LMS.controller;

import com.example.LMS.dto.response.ApiResponse;
import com.example.LMS.dto.response.DashboardStatsDto;
import com.example.LMS.dto.response.MonthlyStatsDto;
import com.example.LMS.dto.response.RecentActivityDto;
import com.example.LMS.model.*;
import com.example.LMS.repository.*;
import com.example.LMS.service.ActivityService;
import com.example.LMS.service.DashboardService;
import com.example.LMS.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
    @Autowired private DashboardService dashboardService;
    @Autowired private ActivityService activityService;
    

    @Autowired
    private DonationRepository donationRepository;
    
    @PostMapping("/refresh")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsDto> refreshStats() {
        // Recalculate and return fresh stats
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Get admin dashboard statistics")
    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAdminDashboardStats() {
        DashboardStatsDto stats = dashboardService.getDashboardStats();
        
        // Use repository methods for efficient counting
        
        return ResponseEntity.ok(new ApiResponse(true, "Dashboard stats retrieved successfully", stats));
    }


    @Operation(summary = "Get member dashboard statistics")
    @GetMapping("/member/{memberId}/stats")
    public ResponseEntity<?> getMemberDashboardStats(@PathVariable Long memberId) {
        Map<String, Object> stats = new HashMap<>();
        


        long totalBooks=bookRepository.count();
        long totalMember=memberRepository.count();
        int currentBorrowedBooks = borrowedBookRepository.countByMemberIdAndStatus(memberId, BorrowStatus.BORROWED);
        long totalBorrowHistory = borrowedBookRepository.findByMemberId(memberId).size();
        long totalComplaints = complaintRepository.findByMemberId(memberId).size();
        long openComplaints = complaintRepository.findByMemberIdAndStatus(memberId, ComplaintStatus.OPEN).size();
        long totalDonations = donationRepository.findByMemberId(memberId).size();
        long pendingDonations = donationRepository.findByMemberIdAndStatus(memberId, DonationStatus.PENDING).size();
//        long totalFines =memberRepository.sumTotalFines();
        

        stats.put("totalBooks", totalBooks);
        stats.put("totalMember", totalMember);
        stats.put("totalBorrowedBooks", currentBorrowedBooks);
        stats.put("totalComplaints", totalComplaints);
        stats.put("pendingComplaints", openComplaints);
        stats.put("overoverdueBooks", 0);
        stats.put("totalFines", 100);
//        stats.put("totalBorrowHistory", totalBorrowHistory);
        
        
//        stats.put("totalDonations", totalDonations);
        stats.put("pendingDonations", pendingDonations);

        return ResponseEntity.ok(new ApiResponse(true, "Member dashboard stats retrieved successfully", stats));
    }
}