package com.example.LMS.dto.response;

import java.time.LocalDateTime;

public class LoginResponse {
    private String token;
    private int expiresIn = 85000000;
    private Long id;
    private String email;
    private String name;
    private String role;
    private boolean isActive;
    private LocalDateTime lastLogin;

    public LoginResponse() {}

    public LoginResponse(String token, Long id, String email, String name, String role,boolean isActive, LocalDateTime lastLogin) {
        this.token = token;
        System.out.println(this.token);
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.isActive=isActive;
        this.lastLogin=lastLogin;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public int getExpiresIn() { return expiresIn; }
    public void setExpiresIn(int expiresIn) { this.expiresIn = expiresIn; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(LocalDateTime lastLogin) {
		this.lastLogin = lastLogin;
	}
}