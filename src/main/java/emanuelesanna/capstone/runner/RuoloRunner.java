package emanuelesanna.capstone.runner;

import emanuelesanna.capstone.entities.Role;
import emanuelesanna.capstone.enums.RoleType;
import emanuelesanna.capstone.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RuoloRunner implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Controlla e crea il ruolo USER se non esiste
        if (roleRepository.findByRole(RoleType.USER).isEmpty()) {
            Role userRole = new Role(RoleType.USER);
            roleRepository.save(userRole);
            System.out.println("Ruolo USER creato nel database.");
        }

        // Controlla e crea il ruolo ADMIN se non esiste
        if (roleRepository.findByRole(RoleType.ADMIN).isEmpty()) {
            Role adminRole = new Role(RoleType.ADMIN);
            roleRepository.save(adminRole);
            System.out.println("Ruolo ADMIN creato nel database.");
        }
    }
}