package com.example.LMS.dto.response;



public class MemberRegistrationResponse {
    private String message;
    private String memberId;
    private String memberName;
    private String email;
    
    public MemberRegistrationResponse() {}
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
