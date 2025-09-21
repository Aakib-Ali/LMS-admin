package com.example.LMS.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LMS.dto.response.DashboardStatsDto;
import com.example.LMS.dto.response.MonthlyStatsDto;
import com.example.LMS.dto.response.RecentActivityDto;
import com.example.LMS.model.Activity;
import com.example.LMS.model.BorrowStatus;
import com.example.LMS.model.ComplaintStatus;
import com.example.LMS.model.DonationStatus;
import com.example.LMS.repository.ActivityRepository;
import com.example.LMS.repository.BookRepository;
import com.example.LMS.repository.BorrowedBookRepository;
import com.example.LMS.repository.ComplaintRepository;
import com.example.LMS.repository.DonationRepository;
import com.example.LMS.repository.MemberRepository;


@Service
public class DashboardService{
    @Autowired private BookRepository bookRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BorrowedBookRepository borrowedBookRepository;
    @Autowired private ComplaintRepository complaintRepository;
    @Autowired private DonationRepository donationRepository;
    @Autowired private ActivityRepository activityRepository; // agar hai

   
    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTotalBooks(bookRepository.count());
        stats.setTotalMembers(memberRepository.count());
        stats.setTotalBorrowedBooks(borrowedBookRepository.countByStatus(BorrowStatus.BORROWED));
        stats.setOverdueBooks(borrowedBookRepository.countOverdueBooks(LocalDateTime.now(), BorrowStatus.BORROWED));
        stats.setTotalComplaints(complaintRepository.count());
        stats.setPendingComplaints(complaintRepository.countByStatus(ComplaintStatus.OPEN));
        stats.setPendingDonations(donationRepository.countByStatus(DonationStatus.PENDING));
        Double totalFines = borrowedBookRepository.sumAllFines();
        
        stats.setTotalFines(totalFines != null ? totalFines : 0.0);

        // MONTHLYSTATS
        stats.setMonthlyStats(getMonthlyStats());

        // RECENT ACTIVITY
        stats.setRecentActivities(getRecentActivities());
        return stats;
    }

    public MonthlyStatsDto getMonthlyStats() {
        LocalDate start = YearMonth.now().atDay(1);
        LocalDate end = YearMonth.now().atEndOfMonth();
        MonthlyStatsDto dto = new MonthlyStatsDto();
        dto.setBooksAdded(bookRepository.countByCreatedDateBetween(start.atStartOfDay(), end.atTime(LocalTime.MAX)));
        dto.setNewMembers(memberRepository.countByRegistrationDateBetween(start.atStartOfDay(), end.atTime(LocalTime.MAX)));
        dto.setBooksIssued(borrowedBookRepository.countByBorrowDateBetween(start.atStartOfDay(), end.atTime(LocalTime.MAX)));
        dto.setComplaintsResolved(complaintRepository.countByStatusAndResolutionDateBetween(
            ComplaintStatus.RESOLVED, start.atStartOfDay(), end.atTime(LocalTime.MAX)));
        
        Double totalFines = borrowedBookRepository.sumFineByMonth(start.atStartOfDay(), end.atTime(LocalTime.MAX));
        if (totalFines == null) totalFines = 0.0;
        dto.setFinesCollected(totalFines);;
        return dto;
    }

    private List<RecentActivityDto> getRecentActivities() {
        return activityRepository.findTop10ByOrderByTimestampDesc()
            .stream().map(Activity::toDto).collect(Collectors.toList());
    }
}
