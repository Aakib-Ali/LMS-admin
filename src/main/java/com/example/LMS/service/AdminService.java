package com.example.LMS.service;

import com.example.LMS.model.Admin;
import com.example.LMS.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Optional<Admin> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Admin saveAdmin(Admin admin) {
        if (admin.getId() == null) {
            admin.setCreatedDate(LocalDateTime.now());
        } else {
            admin.setUpdatedDate(LocalDateTime.now());
        }
        return adminRepository.save(admin);
    }

    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }

    public void updateLastLogin(String email) {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            Admin adminUser = admin.get();
            adminUser.setLastLogin(LocalDateTime.now());
            adminRepository.save(adminUser);
        }
    }
}
