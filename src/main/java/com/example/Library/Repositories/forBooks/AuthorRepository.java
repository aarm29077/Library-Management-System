package com.example.Library.Repositories.forBooks;

import com.example.Library.Models.forBooks.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNameAndSurname(String name, String surname);

    Optional<List<Author>> findByName(String name);

    Optional<List<Author>> findBySurname(String surname);

    Optional<List<Author>> findByNationality(String nationality);

}