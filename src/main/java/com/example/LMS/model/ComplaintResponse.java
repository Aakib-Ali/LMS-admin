package com.example.LMS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "complaint_responses")
//@JsonIgnoreProperties({"hibernateLazyInitializer","handler","complaint"})
public class ComplaintResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @Column(name = "responded_by")
    private String respondedBy="ADMIN";

    @NotBlank
    @Column(name = "response_text", nullable = false, length = 2000)
    private String responseText;

    @Column(name = "response_date", nullable = false)
    private LocalDateTime responseDate = LocalDateTime.now();

    @Column(name = "is_from_admin", nullable = false)
    private Boolean isFromAdmin = false;

    public ComplaintResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Complaint getComplaint() { return complaint; }
    public void setComplaint(Complaint complaint) { this.complaint = complaint; }

    public String getRespondedBy() { return respondedBy; }
    public void setRespondedBy(String respondedBy) { this.respondedBy = respondedBy; }

    public String getResponseText() { return responseText; }
    public void setResponseText(String responseText) { this.responseText = responseText; }

    public LocalDateTime getResponseDate() { return responseDate; }
    public void setResponseDate(LocalDateTime responseDate) { this.responseDate = responseDate; }

    public Boolean getIsFromAdmin() { return isFromAdmin; }
    public void setIsFromAdmin(Boolean isFromAdmin) { this.isFromAdmin = isFromAdmin; }
}