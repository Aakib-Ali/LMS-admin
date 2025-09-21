package com.example.LMS.model;

import java.time.LocalDateTime;

import com.example.LMS.dto.response.RecentActivityDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // 'Book Added', 'Complaint Filed', etc.
    private String description;
    private LocalDateTime timestamp;

    @ManyToOne
    private Member member; // optional, jo bhi activity initiate kare

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
	
	public RecentActivityDto toDto() {
	    RecentActivityDto dto = new RecentActivityDto();
	    dto.setId(String.valueOf(this.id));
	    dto.setType(this.type);
	    dto.setDescription(this.description);
	    dto.setTimestamp(this.timestamp);
	    dto.setMemberId(this.member != null ? String.valueOf(this.member.getId()) : null);
	    dto.setMemberName(this.member != null ? this.member.getName() : null);
	    return dto;
	}


    // getters & setters
    
}
