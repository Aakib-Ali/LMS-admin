package com.example.LMS.dto.response;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.LMS.model.ComplaintResponse;
import com.example.LMS.model.ComplaintStatus;


@Component
public class CompResponse {
	private String id;
	private String memberId;
	private String memberName;
		  private String category;
		  private String title;
		  private String description;
		  private String contactpreference;
		  private LocalDateTime submissionDate;
		  private ComplaintStatus status;
		  private String assignedTo;
		  private String priority;
		  private List<ComplaintResponse> responses;
		  private LocalDateTime resolutionDate;
		  private String resolutionNotes;
		  
		public CompResponse() {
			super();
			// TODO Auto-generated constructor stub
		}
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getMemberId() {
			return memberId;
		}

		public void setMemberId(String memberId) {
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
		public String getContactpreference() {
			return contactpreference;
		}
		public void setContactpreference(String contactpreference) {
			this.contactpreference = contactpreference;
		}
		public LocalDateTime getSubmissionDate() {
			return submissionDate;
		}
		public void setSubmissionDate(LocalDateTime submissionDate) {
			this.submissionDate = submissionDate;
		}
		public ComplaintStatus getStatus() {
			return status;
		}
		public void setStatus(ComplaintStatus status) {
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
		public List<ComplaintResponse> getResponses() {
			return responses;
		}
		public void setResponses(List<ComplaintResponse> responses) {
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
		  
    
    
}