package emanuelesanna.capstone.services;

import emanuelesanna.capstone.entities.Role;
import emanuelesanna.capstone.entities.User;
import emanuelesanna.capstone.enums.RoleType;
import emanuelesanna.capstone.exceptions.BadRequestException;
import emanuelesanna.capstone.exceptions.NotFoundException;
import emanuelesanna.capstone.payload.NewUserDTO;
import emanuelesanna.capstone.payload.UserUpdateDTO;
import emanuelesanna.capstone.repositories.RoleRepository;
import emanuelesanna.capstone.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private RoleRepository roleRepository;

    //    Metodo per salvare un nuovo utente

    public User saveUser(NewUserDTO payload) {

        this.userRepository.findByEmail(payload.email()).ifPresent(utente -> {
            throw new BadRequestException("L'email " + utente.getEmail() + " è già registrata!");
        });

        Role userRole = roleRepository.findByRole(RoleType.USER)
                .orElseThrow(() -> new NotFoundException("Ruolo USER non trovato. Aggiungilo nel database."));

        User newUser = new User();
        newUser.setName(payload.name());
        newUser.setSurname(payload.surname());
        newUser.setEmail(payload.email());
        newUser.setPassword(bcrypt.encode(payload.password()));

        newUser.getRoles().add(userRole);

        User savedUser = this.userRepository.save(newUser);

        log.info("L'utente con id: " + savedUser.getIdUser() + " è stato salvato correttamente");
        return savedUser;
    }

    //    Metodo per trovare un utente tramite id

    public User findById(UUID userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    //    Metodo per trovare un utente tramite id e aggiornarlo

    public User findByIdAndUpdate(UUID userId, UserUpdateDTO payload) {

        User found = this.findById(userId);

        if (!found.getEmail().equalsIgnoreCase(payload.email())) {
            this.userRepository.findByEmail(payload.email()).ifPresent(user -> {

                if (!user.getIdUser().equals(userId)) {
                    throw new BadRequestException("L'email " + payload.email() + " è già registrata!");
                }
            });
        }
        found.setName(payload.name());
        found.setSurname(payload.surname());
        found.setEmail(payload.email());

        User modifiedUser = this.userRepository.save(found);

        log.info("L'utente con id " + modifiedUser.getIdUser() + " è stato modificato correttamente");

        return modifiedUser;
    }

    //    Metodo per trovare un utente tramite id e eliminarlo

    public void findByIdAndDelete(UUID userId) {
        User found = this.findById(userId);
        this.userRepository.delete(found);
    }

    //    Metodo robusto per trovare un utente tramite email

    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con l'email " + email + " non è stato trovato"));
    }
}

