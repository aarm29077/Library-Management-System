package com.example.Library.contoller.users;

import com.example.Library.dto.user.UserDTORequest;
import com.example.Library.models.books.Book;
import com.example.Library.models.users.User;
import com.example.Library.service.books.BookUserService;
import com.example.Library.service.users.UserService;
import com.example.Library.service.conversion.DTOConversionService;
import com.example.Library.util.customAnnotations.ValidString;
import com.sun.jdi.request.InvalidRequestStateException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.Library.util.roles.ROLE.ROLE_ADMIN;
import static com.example.Library.util.roles.ROLE.ROLE_USER;

@RestController
@RequestMapping("/users")
@Validated
@SecurityRequirement(name = "javainuseapi")
public class UserController {
    private final UserService userService;
    private final DTOConversionService dtoConversionService;
    private final BookUserService bookUserService;

    @Autowired
    public UserController(UserService userService, DTOConversionService dtoConversionService, BookUserService bookUserService) {
        this.userService = userService;
        this.dtoConversionService = dtoConversionService;
        this.bookUserService = bookUserService;
    }

    @GetMapping("/all")//ADMIN
    public ResponseEntity<?> getUsers() {
        Optional<List<User>> users = userService.findUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users.get().stream().map(dtoConversionService::convertToUserDTOResponse).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")//ADMIN, USER
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userById = userService.findUserById(id);
        if (userById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dtoConversionService.convertToUserDTOResponse(userById.get()), HttpStatus.OK);
    }

    @GetMapping("/{id}/role") //ADMIN, USER
    public ResponseEntity<?> getUserRole(@PathVariable Long id) {
        Optional<String> userRoleByUserId = userService.findUserRoleByUserId(id);
        if (userRoleByUserId.isEmpty()) {
            return new ResponseEntity<>("The user with given id " + id + " has not role", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("The user's role with id " + id + " is " + userRoleByUserId.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}/books")//ADMIN, USER
    public ResponseEntity<?> getUserBooks(@PathVariable Long id) {

        Optional<List<Book>> userBooks = userService.findUserBooks(id);
        if (userBooks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Book> resultBooks = userBooks.get();
/*
        List<BookUser> bookUsers = new ArrayList<>();
        for (Book book : userBooks.get()) {
            bookUsers.add(bookUserService.findByBookIdAndUserId(book.getId(), id).get());
        }
        bookUsers.sort(Comparator.comparing(BookUser::getTakenAt));

        List<Book> resultBooks = new ArrayList<>();
        for (BookUser bookUser : bookUsers) {
            resultBooks.add(bookUser.getBook());
        }
        */
        resultBooks.sort(Comparator.comparing(book -> bookUserService.findByBookIdAndUserId(book.getId(), id).orElseThrow(InvalidRequestStateException::new).getTakenAt()));

        return new ResponseEntity<>(resultBooks.stream().map(dtoConversionService::convertToBookDTOResponseWithAuthors).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/getByName/{name}")//ADMIN
    public ResponseEntity<?> getUsersByName(@PathVariable @Validated @Size(min = 2, max = 30, message = "The customer's name should be between 2 and 30 characters") @NotBlank(message = "The customer's name should not be empty") @ValidString(message = "The given customer's name is not valid") String name) {
        Optional<List<User>> usersByName = userService.findUsersByName(name);
        if (usersByName.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usersByName.get().stream().map(dtoConversionService::convertToUserDTOResponse).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/getBySurname/{surname}")//ADMIN
    public ResponseEntity<?> getUsersBySurname(@PathVariable @Validated @Size(min = 2, max = 30, message = "The customer's surname should be between 2 and 30 characters") @NotBlank(message = "The customer's surname should not be empty") @ValidString(message = "The given customer's surname is not valid") String surname) {
        Optional<List<User>> usersBySurname = userService.findUsersBySurname(surname);
        if (usersBySurname.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usersBySurname.get().stream().map(dtoConversionService::convertToUserDTOResponse).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/getByEmail/{email}")//ADMIN
    public ResponseEntity<?> getUserByEmail(@PathVariable @Validated @Email(message = "Please provide a valid email address") @ValidString(message = "The given customer's email is not valid") String email) {
        Optional<User> userByEmail = userService.findUserByEmail(email);
        if (userByEmail.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dtoConversionService.convertToUserDTOResponse(userByEmail.get()), HttpStatus.OK);
    }

    @GetMapping("{userId}/books/{bookId}/taken-at")//ADMIN, USER
    public ResponseEntity<LocalDateTime> getTakenAtTime(
            @PathVariable Long userId,
            @PathVariable Long bookId
    ) {
        LocalDateTime takenAt = bookUserService.getTakenAtTime(bookId, userId);
        return ResponseEntity.ok(takenAt);
    }

    @DeleteMapping("/{id}")//ADMIN
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean removed = userService.deleteUser(id);

        if (removed) {
            return new ResponseEntity<>("User with ID " + id + " removed successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User with ID " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    //for credentials too
    @PutMapping("/{id}")//USER
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody @Valid UserDTORequest userDTORequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Optional<User> userById = userService.findUserById(id);
        if (userById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = dtoConversionService.convertToUser(userDTORequest);
        user.setId(userById.get().getId());

        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(dtoConversionService.convertToUserDTOResponse(updatedUser), HttpStatus.OK);
    }

    @PostMapping("/{requesterId}/update-roleToAdmin/{userId}")//Admin
    public ResponseEntity<?> changeUserRoleToAdmin(
            @PathVariable Long requesterId,
            @PathVariable Long userId
    ) throws AccessDeniedException {
        if (!userService.updateUserRole(requesterId, userId, ROLE_ADMIN)) {
            return new ResponseEntity<>("User with id " + userId + "already has role " + ROLE_ADMIN,
                    HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok("User role updated successfully");
    }

    @PostMapping("/{requesterId}/update-roleToUser/{userId}")//Admin
    public ResponseEntity<?> changeUserRoleToUser(
            @PathVariable Long requesterId,
            @PathVariable Long userId
    ) {
        try {
            if (!userService.updateUserRole(requesterId, userId, ROLE_USER)) {
                return new ResponseEntity<>("User with id " + userId + "already has role " + ROLE_USER,
                        HttpStatus.CONFLICT);
            }
            return ResponseEntity.ok("User role updated successfully");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: " + e.getMessage());
        }
    }

/*Register

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid UserDTORequest userDTORequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        boolean inserted = userService.createUser(dtoConversionService.convertToUser(userDTORequest));
        if (inserted) {
            return new ResponseEntity<>("User with Email " + userDTORequest.getEmail() + " is inserted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User with Email " + userDTORequest.getEmail() + " already exists", HttpStatus.BAD_REQUEST);
        }
    }
*/

    @PostMapping("/{userId}/take-book/{bookId}")//USER
    public ResponseEntity<String> takeBook(
            @PathVariable Long userId,
            @PathVariable Long bookId
    ) {
        if (!userService.takeBook(userId, bookId)) {
            return new ResponseEntity<>("User already has book with id " + bookId, HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok("Book added to the user successfully");

    }

    @PostMapping("/{userId}/release-book/{bookId}")//USER
    public ResponseEntity<String> releaseBook(
            @PathVariable Long userId,
            @PathVariable Long bookId
    ) {

        if (!userService.releaseBook(userId, bookId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("Book released successfully");
    }

}
