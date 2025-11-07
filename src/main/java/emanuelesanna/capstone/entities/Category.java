package emanuelesanna.capstone.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"products"})
public class Category {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID idCategory;
    private String name;
    private String description;
    private String slug;
    private String coverImageUrl;
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public Category(String name, String description, String slug, String coverImageUrl) {
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.coverImageUrl = coverImageUrl;
    }
}
