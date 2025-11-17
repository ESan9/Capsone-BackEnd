package emanuelesanna.capstone.controllers;

import emanuelesanna.capstone.entities.User;
import emanuelesanna.capstone.exceptions.ValidationException;
import emanuelesanna.capstone.payload.UserUpdateDTO;
import emanuelesanna.capstone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public User getMe(@AuthenticationPrincipal User currentUser) {
        return currentUser;
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById(@PathVariable UUID userId) {
        return userService.findById(userId);
    }


    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUser(@PathVariable UUID userId,
                           @RequestBody @Validated UserUpdateDTO payload,
                           BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return userService.findByIdAndUpdate(userId, payload);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204 No Content
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable UUID userId) {
        userService.findByIdAndDelete(userId);
    }
}