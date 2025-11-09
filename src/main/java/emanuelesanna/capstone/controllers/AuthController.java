package emanuelesanna.capstone.controllers;

import emanuelesanna.capstone.entities.User;
import emanuelesanna.capstone.exceptions.ValidationException;
import emanuelesanna.capstone.payload.LoginDTO;
import emanuelesanna.capstone.payload.LoginResponseDTO;
import emanuelesanna.capstone.payload.NewUserDTO;
import emanuelesanna.capstone.services.AuthService;
import emanuelesanna.capstone.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    // POST http://localhost:3001/auth/login
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(authService.checkCredentialsAndGenerateToken(body));
    }

    // POST http://localhost:3001/auth/register
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody @Validated NewUserDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {

            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.userService.saveUser(payload);
    }
}
