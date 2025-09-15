package com.example.LMS.controller;

import com.example.LMS.dto.response.ApiResponse;
import com.example.LMS.model.Member;
import com.example.LMS.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Member Management", description = "Member management APIs")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @Operation(summary = "Get all members")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Members retrieved successfully", members));
    }

    @Operation(summary = "Get member by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getMemberById(@PathVariable Long id) {
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, "Member retrieved successfully", member.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update member status")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> updateMemberStatus(@PathVariable Long id, @RequestParam boolean isActive) {
        Optional<Member> memberOpt = memberRepository.findById(id);
        if (!memberOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Member member = memberOpt.get();
        member.setIsActive(isActive);
        memberRepository.save(member);

        return ResponseEntity.ok(new ApiResponse(true, "Member status updated successfully"));
    }
}
