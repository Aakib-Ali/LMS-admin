package com.example.LMS.controller;

import com.example.LMS.dto.request.ComplaintRequest;
import com.example.LMS.dto.response.ApiResponse;
import com.example.LMS.model.*;
import com.example.LMS.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/complaints")
@Tag(name = "Complaints", description = "Complaint management APIs")
public class ComplaintController {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintResponseRepository complaintResponseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Operation(summary = "Get all complaints")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAllComplaints() {
        List<Complaint> complaints = complaintRepository.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Complaints retrieved successfully", complaints));
    }

    @Operation(summary = "Get complaints by member")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getComplaintsByMember(@PathVariable Long memberId) {
        List<Complaint> complaints = complaintRepository.findByMemberId(memberId);
        return ResponseEntity.ok(new ApiResponse(true, "Complaints retrieved successfully", complaints));
    }

    @Operation(summary = "Get complaints by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getComplaintsByStatus(@PathVariable String status) {
        try {
            ComplaintStatus complaintStatus = ComplaintStatus.valueOf(status.toUpperCase());
            List<Complaint> complaints = complaintRepository.findByStatus(complaintStatus);
            return ResponseEntity.ok(new ApiResponse(true, "Complaints retrieved successfully", complaints));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid status"));
        }
    }

    @Operation(summary = "Get complaint by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getComplaintById(@PathVariable Long id) {
        Optional<Complaint> complaint = complaintRepository.findById(id);
        if (complaint.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, "Complaint retrieved successfully", complaint.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "File a new complaint")
    @PostMapping
    public ResponseEntity<?> fileComplaint(@Valid @RequestBody ComplaintRequest request) {
        Optional<Member> memberOpt = memberRepository.findById(request.getMemberId());
        if (!memberOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Member not found"));
        }

        Complaint complaint = new Complaint();
        complaint.setMember(memberOpt.get());
        complaint.setCategory(request.getCategory());
        complaint.setTitle(request.getTitle());
        complaint.setDescription(request.getDescription());
        complaint.setContactPreference(request.getContactPreference());
        complaint.setSubmissionDate(LocalDateTime.now());
        complaint.setStatus(ComplaintStatus.OPEN);
        complaint.setPriority("Medium");

        Complaint savedComplaint = complaintRepository.save(complaint);
        return ResponseEntity.ok(new ApiResponse(true, "Complaint filed successfully", savedComplaint));
    }

    @Operation(summary = "Update complaint status")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateComplaintStatus(@PathVariable Long id, 
                                                 @RequestParam String status,
                                                 @RequestParam(required = false) String assignedTo,
                                                 @RequestParam(required = false) String priority,
                                                 @RequestParam(required = false) String resolutionNotes) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(id);
        if (!complaintOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Complaint complaint = complaintOpt.get();
            complaint.setStatus(ComplaintStatus.valueOf(status.toUpperCase()));
            
            if (assignedTo != null) {
                complaint.setAssignedTo(assignedTo);
            }
            
            if (priority != null) {
                complaint.setPriority(priority);
            }
            
            if (resolutionNotes != null) {
                complaint.setResolutionNotes(resolutionNotes);
            }
            
            if (ComplaintStatus.valueOf(status.toUpperCase()) == ComplaintStatus.RESOLVED || 
                ComplaintStatus.valueOf(status.toUpperCase()) == ComplaintStatus.CLOSED) {
                complaint.setResolutionDate(LocalDateTime.now());
            }

            Complaint updatedComplaint = complaintRepository.save(complaint);
            return ResponseEntity.ok(new ApiResponse(true, "Complaint status updated successfully", updatedComplaint));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid status"));
        }
    }

    @Operation(summary = "Add response to complaint")
    @PostMapping("/{id}/response")
    public ResponseEntity<?> addComplaintResponse(@PathVariable Long id, 
                                                @RequestParam String responseText,
                                                @RequestParam String respondedBy,
                                                @RequestParam(defaultValue = "false") boolean isFromAdmin) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(id);
        if (!complaintOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        ComplaintResponse response = new ComplaintResponse();
        response.setComplaint(complaintOpt.get());
        response.setResponseText(responseText);
        response.setRespondedBy(respondedBy);
        response.setIsFromAdmin(isFromAdmin);
        response.setResponseDate(LocalDateTime.now());

        ComplaintResponse savedResponse = complaintResponseRepository.save(response);
        return ResponseEntity.ok(new ApiResponse(true, "Response added successfully", savedResponse));
    }

    @Operation(summary = "Get responses for a complaint")
    @GetMapping("/{id}/responses")
    public ResponseEntity<?> getComplaintResponses(@PathVariable Long id) {
        List<ComplaintResponse> responses = complaintResponseRepository.findByComplaintIdOrderByResponseDateDesc(id);
        return ResponseEntity.ok(new ApiResponse(true, "Responses retrieved successfully", responses));
    }
}
