package emanuelesanna.capstone.services;

import emanuelesanna.capstone.entities.User;
import emanuelesanna.capstone.exceptions.UnauthorizedException;
import emanuelesanna.capstone.payload.LoginDTO;
import emanuelesanna.capstone.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        User found = this.userService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.createToken(found);
        } else {
            throw new UnauthorizedException("Credenziali errate!");
        }
    }
}
