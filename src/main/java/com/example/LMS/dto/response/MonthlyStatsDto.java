package com.example.LMS.dto.response;

public class MonthlyStatsDto {
    private long booksAdded;
    private long newMembers;
    private long booksIssued;
    private long complaintsResolved;
    private double finesCollected;
	public MonthlyStatsDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getBooksAdded() {
		return booksAdded;
	}
	public void setBooksAdded(long booksAdded) {
		this.booksAdded = booksAdded;
	}
	public long getNewMembers() {
		return newMembers;
	}
	public void setNewMembers(long newMembers) {
		this.newMembers = newMembers;
	}
	public long getBooksIssued() {
		return booksIssued;
	}
	public void setBooksIssued(long booksIssued) {
		this.booksIssued = booksIssued;
	}
	public long getComplaintsResolved() {
		return complaintsResolved;
	}
	public void setComplaintsResolved(long complaintsResolved) {
		this.complaintsResolved = complaintsResolved;
	}
	public double getFinesCollected() {
		return finesCollected;
	}
	public void setFinesCollected(double finesCollected) {
		this.finesCollected = finesCollected;
	}
    
    
}
