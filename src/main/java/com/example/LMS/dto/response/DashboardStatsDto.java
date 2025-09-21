package com.example.LMS.dto.response;

import java.util.List;

public class DashboardStatsDto {
    private long totalBooks;
    private long totalMembers;
    private long totalBorrowedBooks;
    private long totalComplaints;
    private long pendingComplaints;
    private long overdueBooks;
    private double totalFines;
    private long pendingDonations;
    private MonthlyStatsDto monthlyStats;
    private List<RecentActivityDto> recentActivities;
	public DashboardStatsDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getTotalBooks() {
		return totalBooks;
	}
	public void setTotalBooks(long totalBooks) {
		this.totalBooks = totalBooks;
	}
	public long getTotalMembers() {
		return totalMembers;
	}
	public void setTotalMembers(long totalMembers) {
		this.totalMembers = totalMembers;
	}
	public long getTotalBorrowedBooks() {
		return totalBorrowedBooks;
	}
	public void setTotalBorrowedBooks(long totalBorrowedBooks) {
		this.totalBorrowedBooks = totalBorrowedBooks;
	}
	public long getTotalComplaints() {
		return totalComplaints;
	}
	public void setTotalComplaints(long totalComplaints) {
		this.totalComplaints = totalComplaints;
	}
	public long getPendingComplaints() {
		return pendingComplaints;
	}
	public void setPendingComplaints(long pendingComplaints) {
		this.pendingComplaints = pendingComplaints;
	}
	public long getOverdueBooks() {
		return overdueBooks;
	}
	public void setOverdueBooks(long overdueBooks) {
		this.overdueBooks = overdueBooks;
	}
	public double getTotalFines() {
		return totalFines;
	}
	public void setTotalFines(double totalFines) {
		this.totalFines = totalFines;
	}
	public long getPendingDonations() {
		return pendingDonations;
	}
	public void setPendingDonations(long pendingDonations) {
		this.pendingDonations = pendingDonations;
	}
	public MonthlyStatsDto getMonthlyStats() {
		return monthlyStats;
	}
	public void setMonthlyStats(MonthlyStatsDto monthlyStats) {
		this.monthlyStats = monthlyStats;
	}
	public List<RecentActivityDto> getRecentActivities() {
		return recentActivities;
	}
	public void setRecentActivities(List<RecentActivityDto> recentActivities) {
		this.recentActivities = recentActivities;
	}

    // Getters, setters, constructors
    
}
