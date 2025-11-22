package emanuelesanna.capstone.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProductImage {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID idProductImage;
    private String imageUrl;
    private String altText;
    @Column(name = "display_order")
    private int order;
    @ManyToOne
    @JoinColumn(name = "idProduct")
    @JsonIgnore
    private Product product;

    public ProductImage(String imageUrl, String altText, int order) {
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.order = order;
    }
}
