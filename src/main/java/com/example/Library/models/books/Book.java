package com.example.Library.models.books;

import com.example.Library.models.BookUser;
import com.example.Library.util.customAnnotations.ValidString;
import com.example.Library.util.customAnnotations.YearFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.ISBN;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "book", uniqueConstraints = @UniqueConstraint(columnNames = {"isbn"}))
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Getter @Setter Long id;

    @Column(name = "title")
    @Size(min = 2, max = 30, message = "The book's title should be between 2 and 30 characters")
    @NotBlank(message = "The book's title should not be empty")
    @ValidString(message = "The given title is not valid")
    private @Getter @Setter String title;

    @Column(name = "publication_date")
    @YearFormat()
    @NotBlank(message = "publicationDate should not be empty")
    private @Getter @Setter String publicationDate;

    @Column(name = "ISBN", nullable = false, unique = true)
    @ISBN(message = "It isn't ISBN format")
    @ValidString(message = "The given ISBN is not valid")
    @NotBlank(message = "The ISBN should not be empty")
    private @Getter @Setter String isbn;

    @Column(name = "version")
    @Version
    private Long version;

    @OneToOne(mappedBy = "book")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private BookStock bookStock;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private @Getter @Setter List<Author> authors ;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookUser> users ;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<BookUser> getUsers() {
        return users;
    }

    public void setUsers(List<BookUser> users) {
        this.users = users;
    }

    public BookStock getBookStock() {
        return bookStock;
    }

    public void setBookStock(BookStock bookStock) {
        this.bookStock = bookStock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(version, book.version) && Objects.equals(id, book.id) && Objects.equals(title, book.title) && Objects.equals(publicationDate, book.publicationDate) && Objects.equals(isbn, book.isbn) && Objects.equals(bookStock, book.bookStock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, publicationDate, isbn, version, bookStock);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", publicationDate=" + publicationDate +
                ", isbn='" + isbn + '\'' +
                ", bookStock=" + bookStock +
                ", authors=" + authors +
                '}';
    }
}
