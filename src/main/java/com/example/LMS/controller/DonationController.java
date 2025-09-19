package com.example.LMS.controller;

import com.example.LMS.dto.request.DonationRequest;
import com.example.LMS.dto.response.ApiResponse;
import com.example.LMS.dto.response.DonationResponse;
import com.example.LMS.model.*;
import com.example.LMS.repository.*;
import com.example.LMS.service.DonationService;

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
@RequestMapping("/api/donations")
@Tag(name = "Donations", description = "Donation management APIs")
public class DonationController {

    @Autowired
    private DonationRepository donationRepository;
    
    @Autowired
    private DonationService donationService;

    @Autowired
    private MemberRepository memberRepository;
    
    @Operation(summary = "Get Pending Request Count")
    @GetMapping("/pending-count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPendingCount(){
    	DonationStatus donationStatus = DonationStatus.valueOf("PENDING");
        List<Donation> donations = donationRepository.findByStatus(donationStatus);
    	return ResponseEntity.ok(new ApiResponse(true,"Donations count Successfully",donations.size()));
    }

    @Operation(summary = "Get all donations")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAllDonations() {
        List<DonationResponse> donations = donationService.getAllDonations();
        return ResponseEntity.ok(new ApiResponse(true, "Donations retrieved successfully", donations));
    }

    @Operation(summary = "Get donations by member")
    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getDonationsByMember(@PathVariable Long memberId) {
        List<Donation> donations = donationRepository.findByMemberId(memberId);
        return ResponseEntity.ok(new ApiResponse(true, "Donations retrieved successfully", donations));
    }

    @Operation(summary = "Get donations by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getDonationsByStatus(@PathVariable String status) {
        try {
            DonationStatus donationStatus = DonationStatus.valueOf(status);
            List<Donation> donations = donationRepository.findByStatus(donationStatus);
            return ResponseEntity.ok(new ApiResponse(true, "Donations retrieved successfully", donations));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid status"));
        }
    }

    @Operation(summary = "Submit a donation")
    @PostMapping
    public ResponseEntity<?> submitDonation(@Valid @RequestBody DonationRequest request) {
        Optional<Member> memberOpt = memberRepository.findById(request.getMemberId());
        if (!memberOpt.isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Member not found"));
        }

        Donation donation = new Donation();
        donation.setMember(memberOpt.get());
        donation.setBookTitle(request.getBookTitle());
        donation.setAuthor(request.getAuthor());
        donation.setCondition(request.getCondition());
        donation.setQuantity(request.getQuantity());
        donation.setDescription(request.getDescription());
        donation.setImageUrl(request.getImageUrl());
        donation.setSubmissionDate(LocalDateTime.now());
        donation.setStatus(DonationStatus.PENDING);

        Donation savedDonation = donationRepository.save(donation);
        return ResponseEntity.ok(new ApiResponse(true, "Donation submitted successfully", savedDonation));
    }

    @Operation(summary = "Review donation")
    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> reviewDonation(@PathVariable Long id,
                                          @RequestParam String status,
                                          @RequestParam String reviewedBy,
                                          @RequestParam(required = false) String adminComments) {
        Optional<Donation> donationOpt = donationRepository.findById(id);
        if (!donationOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            Donation donation = donationOpt.get();
            donation.setStatus(DonationStatus.valueOf(status.toUpperCase()));
            donation.setReviewedBy(reviewedBy);
            donation.setReviewDate(LocalDateTime.now());
            
            if (adminComments != null) {
                donation.setAdminComments(adminComments);
            }

            Donation updatedDonation = donationRepository.save(donation);
            return ResponseEntity.ok(new ApiResponse(true, "Donation reviewed successfully", updatedDonation));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid status"));
        }
    }

    @Operation(summary = "Get donation by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getDonationById(@PathVariable Long id) {
        Optional<Donation> donation = donationRepository.findById(id);
        if (donation.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, "Donation retrieved successfully", donation.get()));
        }
        return ResponseEntity.notFound().build();
    }
}
