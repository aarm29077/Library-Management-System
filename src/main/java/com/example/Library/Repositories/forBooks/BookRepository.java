package com.example.Library.Repositories.forBooks;

import com.example.Library.Models.forBooks.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    void deleteByIsbn(String isbn);

    Optional<List<Book>> findByTitle(String title);

    Optional<Book> findByIsbn(String isbn);

}