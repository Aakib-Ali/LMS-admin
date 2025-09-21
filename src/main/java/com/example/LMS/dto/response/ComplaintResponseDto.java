package com.example.LMS.dto.response;

import java.time.LocalDateTime;

public class ComplaintResponseDto {
    private Long id;
    private String respondedBy;
    private String responseText;
    private LocalDateTime responseDate;
    private Boolean isFromAdmin;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRespondedBy() {
		return respondedBy;
	}
	public void setRespondedBy(String respondedBy) {
		this.respondedBy = respondedBy;
	}
	public String getResponseText() {
		return responseText;
	}
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}
	public LocalDateTime getResponseDate() {
		return responseDate;
	}
	public void setResponseDate(LocalDateTime responseDate) {
		this.responseDate = responseDate;
	}
	public Boolean getIsFromAdmin() {
		return isFromAdmin;
	}
	public void setIsFromAdmin(Boolean isFromAdmin) {
		this.isFromAdmin = isFromAdmin;
	}
	public ComplaintResponseDto() {
		super();
		// TODO Auto-generated constructor stub
	}

    // Getters and setters...
    
    
}
