package emanuelesanna.capstone.runner;

import emanuelesanna.capstone.entities.Role;
import emanuelesanna.capstone.entities.User;
import emanuelesanna.capstone.enums.RoleType;
import emanuelesanna.capstone.repositories.RoleRepository;
import emanuelesanna.capstone.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder bcrypt;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.findByEmail(adminEmail).isEmpty()) {

            Role adminRole = roleRepository.findByRole(RoleType.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Errore: Ruolo ADMIN non trovato. Esegui prima RuoloRunner?"));

            User newAdmin = new User();
            newAdmin.setName("Admin");
            newAdmin.setSurname("Shop");
            newAdmin.setEmail(adminEmail);
            newAdmin.setPassword(bcrypt.encode(adminPassword));

            newAdmin.getRoles().add(adminRole);

            userRepository.save(newAdmin);

            System.out.println("Utente ADMIN creato con email: " + adminEmail);
        }
    }
}