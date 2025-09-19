package com.example.LMS.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.LMS.dto.response.BookResponse;
import com.example.LMS.dto.response.CompResponse;
import com.example.LMS.dto.response.DonationResponse;
import com.example.LMS.dto.response.MemberResponse;
import com.example.LMS.model.Book;
import com.example.LMS.model.Complaint;
import com.example.LMS.model.Donation;
import com.example.LMS.model.Member;

@Component
public class Mapper {
	public BookResponse toResponse(Book book) {
		BookResponse dto=new BookResponse();
		dto.setId(book.getBookId());
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
	public MemberResponse  toResponse(Member mem) {
		MemberResponse dto=new MemberResponse();
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
	public CompResponse toResponse(Complaint com) {
		CompResponse dto=new CompResponse();
		dto.setId(com.getId().toString());
		dto.setMemberId(com.getMember().getMemberId());
		dto.setMemberName(com.getMember().getName());
		dto.setTitle(com.getTitle());
		dto.setAssignedTo(com.getAssignedTo());
		dto.setCategory(com.getCategory());
		dto.setContactpreference(com.getContactPreference());
		dto.setDescription(com.getDescription());
		dto.setPriority(com.getPriority());
		dto.setResolutionDate(com.getResolutionDate());
		dto.setResolutionNotes(com.getResolutionNotes());
		dto.setStatus(com.getStatus());
		dto.setSubmissionDate(com.getSubmissionDate());
		dto.setResponses(com.getResponses());
		return dto;
	}
	public DonationResponse toResponse(Donation don) {
		DonationResponse dto=new DonationResponse();
		dto.setId(don.getId().toString());
		dto.setMemeberId(don.getMember().getId().toString());
		dto.setMemberName(don.getMember().getName());
		dto.setBookTitle(don.getBookTitle());
		dto.setAuthor(don.getAuthor());
		dto.setCondition(don.getCondition());
		dto.setQuantity(don.getQuantity());
		dto.setDescription(don.getDescription());
		dto.setImageUrl(don.getImageUrl()!=null?don.getImageUrl():null);
		dto.setSubmissionDate(don.getSubmissionDate());
		dto.setStatus(don.getStatus().name());
		dto.setAdminComments(don.getAdminComments()!=""?"":don.getAdminComments());
		dto.setReviewedBy(don.getReviewedBy()!=null?don.getReviewedBy():"");
		dto.setReviewDate(don.getReviewDate());
		return dto;
	}

}