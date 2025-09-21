package com.example.LMS.dto.response;

import com.example.LMS.model.ComplaintStatus;
import java.time.LocalDateTime;
import java.util.List;

public class ComplaintDto {
    private Long id;
    private Long memberId;
    private String memberName;
    private String category;
    private String title;
    private String description;
    private String contactPreference;
    private LocalDateTime submissionDate;
    private String status;
    private String assignedTo;
    private String priority;
    private List<ComplaintResponseDto> responses;
    private LocalDateTime resolutionDate;
    private String resolutionNotes;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContactPreference() {
		return contactPreference;
	}
	public void setContactPreference(String contactPreference) {
		this.contactPreference = contactPreference;
	}
	public LocalDateTime getSubmissionDate() {
		return submissionDate;
	}
	public void setSubmissionDate(LocalDateTime submissionDate) {
		this.submissionDate = submissionDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public List<ComplaintResponseDto> getResponses() {
		return responses;
	}
	public void setResponses(List<ComplaintResponseDto> responses) {
		this.responses = responses;
	}
	public LocalDateTime getResolutionDate() {
		return resolutionDate;
	}
	public void setResolutionDate(LocalDateTime resolutionDate) {
		this.resolutionDate = resolutionDate;
	}
	public String getResolutionNotes() {
		return resolutionNotes;
	}
	public void setResolutionNotes(String resolutionNotes) {
		this.resolutionNotes = resolutionNotes;
	}
	public ComplaintDto() {
		super();
		// TODO Auto-generated constructor stub
	}

    // Getters and setters...
    
}
