package emanuelesanna.capstone.entities;

import emanuelesanna.capstone.enums.Availability;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"productImages"})
public class Product {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID idProduct;
    private String name;
    private String description;
    private BigDecimal price;
    private String slug;
    @Enumerated(EnumType.STRING)
    private Availability availability;
    private boolean highlighted;
    private String materials;
    private String dimension;
    @ManyToOne
    @JoinColumn(name = "idCategory")
    private Category category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();

    public Product(String name, String description, BigDecimal price, String slug, Availability availability, boolean highlighted, String materials, String dimension) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.slug = slug;
        this.availability = availability;
        this.highlighted = highlighted;
        this.materials = materials;
        this.dimension = dimension;
    }
}
