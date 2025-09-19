package com.example.LMS.service;

import com.example.LMS.mapper.Mapper;
import com.example.LMS.dto.response.CompResponse;
import com.example.LMS.model.*;
import com.example.LMS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;
    
    @Autowired
    private Mapper mapper;

    @Autowired
    private ComplaintResponseRepository complaintResponseRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<CompResponse> getAllComplaints() {
    	List<CompResponse> compResponses=new ArrayList<CompResponse>();
        List<Complaint> complaints=complaintRepository.findAll();
        
        for(Complaint complaint:complaints) {
        	System.out.println(complaint.getMember().getId());
        	compResponses.add(mapper.toResponse(complaint));
        	
        }
        return compResponses;
    }

    public List<CompResponse> getComplaintsByMember(Long memberId) {
    	List<CompResponse> compResponses=new ArrayList<CompResponse>();
        List<Complaint> complaints=complaintRepository.findByMemberId(memberId);
        for(Complaint complaint:complaints) {
        	compResponses.add(mapper.toResponse(complaint));
        }
        return compResponses;
    }

    public List<Complaint> getComplaintsByStatus(ComplaintStatus status) {
        return complaintRepository.findByStatus(status);
    }

    public Optional<CompResponse> getComplaintById(Long id) {
        return complaintRepository.findById(id)
        		.map(complaint->mapper.toResponse(complaint));
    }

    @Transactional
    public Complaint fileComplaint(Long memberId, String category, String title, 
                                  String description, String contactPreference) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (!memberOpt.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        Complaint complaint = new Complaint();
        complaint.setMember(memberOpt.get());
        complaint.setCategory(category);
        complaint.setTitle(title);
        complaint.setDescription(description);
        complaint.setContactPreference(contactPreference);
        complaint.setSubmissionDate(LocalDateTime.now());
        complaint.setStatus(ComplaintStatus.OPEN);
        complaint.setPriority("Medium");

        return complaintRepository.save(complaint);
    }

    @Transactional
    public Complaint updateComplaintStatus(Long complaintId, ComplaintStatus status, 
                                         String assignedTo, String priority, String resolutionNotes) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(complaintId);
        if (!complaintOpt.isPresent()) {
            throw new RuntimeException("Complaint not found");
        }

        Complaint complaint = complaintOpt.get();
        complaint.setStatus(status);
        
        if (assignedTo != null) {
            complaint.setAssignedTo(assignedTo);
        }
        
        if (priority != null) {
            complaint.setPriority(priority);
        }
        
        if (resolutionNotes != null) {
            complaint.setResolutionNotes(resolutionNotes);
        }
        
        if (status == ComplaintStatus.RESOLVED || status == ComplaintStatus.CLOSED) {
            complaint.setResolutionDate(LocalDateTime.now());
        }

        return complaintRepository.save(complaint);
    }

    @Transactional
    public ComplaintResponse addResponse(Long complaintId, String responseText, 
                                       String respondedBy, boolean isFromAdmin) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(complaintId);
        if (!complaintOpt.isPresent()) {
            throw new RuntimeException("Complaint not found");
        }

        ComplaintResponse response = new ComplaintResponse();
        response.setComplaint(complaintOpt.get());
        response.setResponseText(responseText);
        response.setRespondedBy(respondedBy);
        response.setIsFromAdmin(isFromAdmin);
        response.setResponseDate(LocalDateTime.now());

        return complaintResponseRepository.save(response);
    }

    public List<ComplaintResponse> getComplaintResponses(Long complaintId) {
        return complaintResponseRepository.findByComplaintIdOrderByResponseDateDesc(complaintId);
    }
}