package com.example.LMS.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DonationRequest {
    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotBlank(message = "Book title is required")
    private String bookTitle;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Condition is required")
    private String condition;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private String description;
    private String imageUrl;

    public DonationRequest() {}

    public Long getMemberId() { return memberId; }
    public void setMemberId(Long memberId) { this.memberId = memberId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}