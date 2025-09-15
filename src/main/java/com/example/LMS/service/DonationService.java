package com.example.LMS.service;

import com.example.LMS.model.*;
import com.example.LMS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private MemberRepository memberRepository;

    public List<Donation> getAllDonations() {
        return donationRepository.findAll();
    }

    public List<Donation> getDonationsByMember(Long memberId) {
        return donationRepository.findByMemberId(memberId);
    }

    public List<Donation> getDonationsByStatus(DonationStatus status) {
        return donationRepository.findByStatus(status);
    }

    public Optional<Donation> getDonationById(Long id) {
        return donationRepository.findById(id);
    }

    @Transactional
    public Donation submitDonation(Long memberId, String bookTitle, String author, 
                                 String condition, Integer quantity, String description, String imageUrl) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (!memberOpt.isPresent()) {
            throw new RuntimeException("Member not found");
        }

        Donation donation = new Donation();
        donation.setMember(memberOpt.get());
        donation.setBookTitle(bookTitle);
        donation.setAuthor(author);
        donation.setCondition(condition);
        donation.setQuantity(quantity);
        donation.setDescription(description);
        donation.setImageUrl(imageUrl);
        donation.setSubmissionDate(LocalDateTime.now());
        donation.setStatus(DonationStatus.PENDING);

        return donationRepository.save(donation);
    }

    @Transactional
    public Donation reviewDonation(Long donationId, DonationStatus status, 
                                 String reviewedBy, String adminComments) {
        Optional<Donation> donationOpt = donationRepository.findById(donationId);
        if (!donationOpt.isPresent()) {
            throw new RuntimeException("Donation not found");
        }

        Donation donation = donationOpt.get();
        donation.setStatus(status);
        donation.setReviewedBy(reviewedBy);
        donation.setReviewDate(LocalDateTime.now());
        
        if (adminComments != null) {
            donation.setAdminComments(adminComments);
        }

        return donationRepository.save(donation);
    }
}
