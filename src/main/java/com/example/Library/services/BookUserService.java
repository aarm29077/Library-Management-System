package com.example.Library.services;

import com.example.Library.models.BookUser;
import com.example.Library.repositories.BookUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookUserService {
    private final BookUserRepository bookUserRepository;

    @Autowired
    public BookUserService(BookUserRepository bookUserRepository) {
        this.bookUserRepository = bookUserRepository;
    }

    public LocalDateTime getTakenAtTime(Long bookId, Long customerId) {
        Optional<BookUser> bookUser = bookUserRepository.findByBookIdAndUserId(bookId, customerId);

        if (bookUser.isPresent()) {
            return bookUser.get().getTakenAt();
        } else {
            throw new EntityNotFoundException("Book not found for the given user.");
        }
    }

    public Optional<BookUser> findByBookIdAndCustomerId(Long bookId, Long userId) {
        return bookUserRepository.findByBookIdAndUserId(bookId, userId);
    }
}