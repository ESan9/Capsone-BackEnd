package emanuelesanna.capstone.entities;

import emanuelesanna.capstone.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(exclude = {"users"})
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID idRole;
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    public Role(RoleType role) {
        this.role = role;
    }
}
