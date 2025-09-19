package com.example.LMS.service;

import com.example.LMS.mapper.Mapper;
import com.example.LMS.dto.response.MemberResponse;
import com.example.LMS.model.Member;
import com.example.LMS.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    Mapper mapper;

    public List<MemberResponse> getAllMembers() {
    	List<MemberResponse> memberResponses=new ArrayList<MemberResponse>();
        List<Member> members=memberRepository.findAll();
        for(Member member:members) {
        	memberResponses.add(mapper.toResponse(member));
        }
        return memberResponses;
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member saveMember(Member member) {
        if (member.getId() == null) {
            member.setRegistrationDate(LocalDateTime.now());
        }
        return memberRepository.save(member);
    }

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean existsByMobileNumber(String mobileNumber) {
        return memberRepository.existsByMobileNumber(mobileNumber);
    }

    public void updateMemberStats(Long memberId, int borrowedBooksChange, double finesChange) {
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            member.setCurrentBorrowedBooks(member.getCurrentBorrowedBooks() + borrowedBooksChange);
            member.setTotalFines(member.getTotalFines() + finesChange);
            memberRepository.save(member);
        }
    }
    
//    public Long getTotalFines() {
//    	return memberRepository.sumTotalFines();
//    }
}