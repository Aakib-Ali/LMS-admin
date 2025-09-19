package com.example.LMS.dto.response;

import org.springframework.stereotype.Component;

import com.example.LMS.model.Role;


@Component
public class MemberResponse {
    private String name;
    private String email;
    private String mobileNumber;
    private String address;
    private Role role = Role.MEMBER;
    private Boolean isActive = true;
    private Integer currentBorrowedBooks = 0;
    private Double totalFines = 0.0;
    private String id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Integer getCurrentBorrowedBooks() {
		return currentBorrowedBooks;
	}
	public void setCurrentBorrowedBooks(Integer currentBorrowedBooks) {
		this.currentBorrowedBooks = currentBorrowedBooks;
	}
	public Double getTotalFines() {
		return totalFines;
	}
	public void setTotalFines(Double totalFines) {
		this.totalFines = totalFines;
	}
	public String getId() {
		return id;
	}
	public void setId(String memberId) {
		this.id = memberId;
	}
	public MemberResponse() {
	}
    
    
    
}