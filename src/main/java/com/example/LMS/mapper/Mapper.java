package com.example.LMS.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.LMS.dto.response.BookResponse;
import com.example.LMS.dto.response.ComplaintDto;
import com.example.LMS.dto.response.ComplaintResponseDto;
import com.example.LMS.dto.response.DonationResponse;
import com.example.LMS.dto.response.MemberResponse;
import com.example.LMS.model.Book;
import com.example.LMS.model.Complaint;
import com.example.LMS.model.ComplaintResponse;
import com.example.LMS.model.Donation;
import com.example.LMS.model.Member;

@Component
public class Mapper {
	
	
    public BookResponse toResponse(Book book) {
        BookResponse dto = new BookResponse();
        dto.setId(book.getId().toString());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setCategory(book.getCategory());
        dto.setDescription(book.getDescription());
        dto.setImageUrl(book.getImageUrl());
        dto.setTotalCopies(book.getTotalCopies());
        dto.setAvailableCopies(book.getAvailableCopies());
        dto.setAvailabilityStatus(book.getStatus().name());
        dto.setActive(book.getIsActive());
        return dto;
    }

    public MemberResponse toResponse(Member mem) {
        MemberResponse dto = new MemberResponse();
        dto.setId(mem.getId().toString());
        dto.setName(mem.getName());
        dto.setCurrentBorrowedBooks(mem.getCurrentBorrowedBooks());
        dto.setEmail(mem.getEmail());
        dto.setIsActive(mem.getIsActive());
        dto.setMobileNumber(mem.getMobileNumber());
        dto.setRole(mem.getRole());
        dto.setTotalFines(mem.getTotalFines());
        return dto;
    }

    public ComplaintDto toResponse(Complaint complaint) {
        ComplaintDto dto = new ComplaintDto();
        dto.setId(complaint.getId());
        dto.setMemberId(complaint.getMember().getId());
        dto.setMemberName(complaint.getMember().getName());
        dto.setCategory(complaint.getCategory());
        dto.setTitle(complaint.getTitle());
        dto.setDescription(complaint.getDescription());
        dto.setContactPreference(complaint.getContactPreference());
        dto.setSubmissionDate(complaint.getSubmissionDate());
        dto.setStatus(complaint.getStatus().name());
        dto.setAssignedTo(complaint.getAssignedTo());
        dto.setPriority(complaint.getPriority());
        dto.setResolutionDate(complaint.getResolutionDate());
        dto.setResolutionNotes(complaint.getResolutionNotes());
        if (complaint.getResponses() != null) {
            // Use toResponse instead of toDto to match method name
            dto.setResponses(complaint.getResponses()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList()));
        }
        return dto;
    }

    public DonationResponse toResponse(Donation don) {
        DonationResponse dto = new DonationResponse();
        dto.setId(don.getId().toString());
        dto.setMemeberId(don.getMember().getId().toString()); // fixed typo: setMemberId
        dto.setMemberName(don.getMember().getName());
        dto.setBookTitle(don.getBookTitle());
        dto.setAuthor(don.getAuthor());
        dto.setCondition(don.getCondition());
        dto.setQuantity(don.getQuantity());
        dto.setDescription(don.getDescription());
        dto.setImageUrl(don.getImageUrl() != null ? don.getImageUrl() : null);
        dto.setSubmissionDate(don.getSubmissionDate());
        dto.setStatus(don.getStatus().name());
        dto.setAdminComments(don.getAdminComments() != null && !don.getAdminComments().isEmpty() ? don.getAdminComments() : "");
        dto.setReviewedBy(don.getReviewedBy() != null ? don.getReviewedBy() : "");
        dto.setReviewDate(don.getReviewDate());
        return dto;
    }

    public ComplaintResponseDto toResponse(ComplaintResponse response) {
        ComplaintResponseDto dto = new ComplaintResponseDto();
        dto.setId(response.getId());
        dto.setRespondedBy(response.getRespondedBy());
        dto.setResponseText(response.getResponseText());
        dto.setResponseDate(response.getResponseDate());
        dto.setIsFromAdmin(response.getIsFromAdmin());
        return dto;
    }
}
