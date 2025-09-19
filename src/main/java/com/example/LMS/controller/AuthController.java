
package com.example.LMS.controller;

import com.example.LMS.dto.request.AdminRegistrationRequest;
import com.example.LMS.dto.request.LoginRequest;
import com.example.LMS.dto.request.MemberRegistrationRequest;
import com.example.LMS.dto.response.ApiResponse;
import com.example.LMS.dto.response.LoginResponse;
import com.example.LMS.model.Admin;
import com.example.LMS.model.Member;
import com.example.LMS.model.Role;
import com.example.LMS.repository.AdminRepository;
import com.example.LMS.repository.MemberRepository;
import com.example.LMS.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Admin Login")
    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            Optional<Admin> admin = adminRepository.findByEmail(loginRequest.getEmail());
            if (admin.isPresent()) {
                Admin adminUser = admin.get();
                adminUser.setLastLogin(LocalDateTime.now());
                adminRepository.save(adminUser);

                String token = jwtUtil.generateToken(adminUser.getEmail(), adminUser.getRole().name(), adminUser.getId());
                System.out.println(token);
                
                LoginResponse loginResponse = new LoginResponse(token, adminUser.getId(), 
                    adminUser.getEmail(), adminUser.getName(), adminUser.getRole().name(),adminUser.getIsActive(),adminUser.getLastLogin());
                
                return ResponseEntity.ok(new ApiResponse(true, "Login successful", loginResponse));
            }
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Admin not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid credentials"));
        }
    }

    @Operation(summary = "Member Login")
    @PostMapping("/member/login")
    public ResponseEntity<?> memberLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            Optional<Member> member = memberRepository.findByEmail(loginRequest.getEmail());
            if (member.isPresent()) {
                Member memberUser = member.get();
                
                String token = jwtUtil.generateToken(memberUser.getEmail(), memberUser.getRole().name(), memberUser.getId());
                
                LoginResponse loginResponse = new LoginResponse(token, memberUser.getId(), 
                    memberUser.getEmail(), memberUser.getName(), memberUser.getRole().name(),memberUser.getIsActive(),memberUser.getLastLogin());
                
                return ResponseEntity.ok(new ApiResponse(true, "Login successful", loginResponse));
            }
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Member not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid credentials"));
        }
    }

    @Operation(summary = "Admin Registration")
    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminRegistrationRequest request) {
        if (adminRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email already exists"));
        }

        Admin admin = new Admin();
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.valueOf(request.getRole().toUpperCase()));
        admin.setIsActive(true);
        admin.setCreatedDate(LocalDateTime.now());

        adminRepository.save(admin);
        return ResponseEntity.ok(new ApiResponse(true, "Admin registered successfully"));
    }

    @Operation(summary = "Member Registration")
    @PostMapping("/member/register")
    public ResponseEntity<?> registerMember(@Valid @RequestBody MemberRegistrationRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email already exists"));
        }

        if (memberRepository.existsByMobileNumber(request.getMobileNumber())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Mobile number already exists"));
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Passwords do not match"));
        }

        Member member = new Member();
        member.setName(request.getMemberName());
        member.setEmail(request.getEmail());
        member.setMobileNumber(request.getMobileNumber());
        member.setAddress(request.getAddress());
        member.setDateOfBirth(request.getDateOfBirth());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setSecretQuestion(request.getSecretQuestion());
        member.setSecretAnswer(request.getSecretAnswer());
        member.setRole(Role.MEMBER);
        member.setIsActive(true);
        member.setRegistrationDate(LocalDateTime.now());

        memberRepository.save(member);
        return ResponseEntity.ok(new ApiResponse(true, "Member registered successfully"));
    }
}