package com.example.LMS.service;

import com.example.LMS.model.Admin;
import com.example.LMS.model.Member;
import com.example.LMS.repository.AdminRepository;
import com.example.LMS.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            Admin adminUser = admin.get();
            List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + adminUser.getRole().name())
            );
            return new User(adminUser.getEmail(), adminUser.getPassword(), 
                          adminUser.getIsActive(), true, true, true, authorities);
        }

        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            Member memberUser = member.get();
            List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + memberUser.getRole().name())
            );
            return new User(memberUser.getEmail(), memberUser.getPassword(), 
                          memberUser.getIsActive(), true, true, true, authorities);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
