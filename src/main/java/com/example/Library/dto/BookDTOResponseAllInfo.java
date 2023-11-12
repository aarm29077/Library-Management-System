package com.example.Library.dto;

import com.example.Library.util.customAnnotations.ValidString;
import com.example.Library.util.customAnnotations.YearFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.ISBN;

import java.util.List;

public class BookDTOResponseAllInfo {

    private Long id;

    @Size(min = 2, max = 30, message = "The book's title should be between 2 and 30 characters")
    @NotBlank(message = "The book's title should not be empty")
    @ValidString(message = "The given title is not valid")
    private String title;

    @YearFormat()
    @NotBlank(message = "publicationDate should not be empty")
    private String publicationDate;

    @ISBN(message = "It isn't ISBN format")
    @ValidString(message = "The given ISBN is not valid")
    @NotBlank(message = "The ISBN should not be empty")
    private String isbn;

    @Min(value = 0, message = "Minimum current quantity should be 0")
    private int currentQuantity;

    @Min(value = 1, message = "Minimum total quantity should be 1")
    private int totalQuantity;

    @NotEmpty(message = "Authors should not be empty")
    private List<AuthorDTOResponse> authors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public List<AuthorDTOResponse> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorDTOResponse> authors) {
        this.authors = authors;
    }
}