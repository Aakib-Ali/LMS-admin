package com.example.LMS.config;

import com.example.LMS.model.*;
import com.example.LMS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!adminRepository.existsByEmail("admin@lms.com")) {
            Admin superAdmin = new Admin();
            superAdmin.setName("Super Administrator");
            superAdmin.setEmail("admin@lms.com");
            superAdmin.setPassword(passwordEncoder.encode("admin123"));
            superAdmin.setRole(Role.SUPER_ADMIN);
            superAdmin.setIsActive(true);
            superAdmin.setCreatedDate(LocalDateTime.now());
            adminRepository.save(superAdmin);
        }

        if (!adminRepository.existsByEmail("librarian@lms.com")) {
            Admin admin = new Admin();
            admin.setName("Head Librarian");
            admin.setEmail("librarian@lms.com");
            admin.setPassword(passwordEncoder.encode("librarian123"));
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);
            admin.setCreatedDate(LocalDateTime.now());
            adminRepository.save(admin);
        }

        if (!memberRepository.existsByEmail("member@test.com")) {
            Member member = new Member();
            member.setName("John Doe");
            member.setEmail("member@test.com");
            member.setMobileNumber("1234567890");
            member.setAddress("123 Main Street, City, State");
            member.setDateOfBirth(LocalDate.of(1990, 1, 1));
            member.setPassword(passwordEncoder.encode("member123"));
            member.setRole(Role.MEMBER);
            member.setIsActive(true);
            member.setRegistrationDate(LocalDateTime.now());
            memberRepository.save(member);
        }

        if (bookRepository.count() == 0) {
            createSampleBooks();
        }
    }

    private void createSampleBooks() {
        String[] titles = {
            "The Great Gatsby", "To Kill a Mockingbird", "1984", "Pride and Prejudice",
            "The Catcher in the Rye", "Lord of the Rings", "Harry Potter", "The Hobbit",
            "Moby Dick", "War and Peace"
        };

        String[] authors = {
            "F. Scott Fitzgerald", "Harper Lee", "George Orwell", "Jane Austen",
            "J.D. Salinger", "J.R.R. Tolkien", "J.K. Rowling", "J.R.R. Tolkien",
            "Herman Melville", "Leo Tolstoy"
        };

        String[] categories = {
            "Fiction", "Fiction", "Dystopian", "Romance",
            "Coming-of-age", "Fantasy", "Fantasy", "Fantasy",
            "Adventure", "Historical Fiction"
        };

        for (int i = 0; i < titles.length; i++) {
            Book book = new Book();
            book.setTitle(titles[i]);
            book.setAuthor(authors[i]);
            book.setCategory(categories[i]);
            book.setDescription("A classic book from the literature collection.");
            book.setTotalCopies(5);
            book.setAvailableCopies(5);
            book.setStatus(BookStatus.AVAILABLE);
            book.setIsActive(true);
            book.setCreatedDate(LocalDateTime.now());
            book.setAddedBy("System");
            bookRepository.save(book);
        }
    }
}
