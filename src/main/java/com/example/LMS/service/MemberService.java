package com.example.LMS.service;

import com.example.LMS.model.Member;
import com.example.LMS.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
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
}
