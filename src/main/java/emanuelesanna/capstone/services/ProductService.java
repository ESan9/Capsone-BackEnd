package emanuelesanna.capstone.services;

import com.cloudinary.Cloudinary;
import emanuelesanna.capstone.entities.Product;
import emanuelesanna.capstone.exceptions.BadRequestException;
import emanuelesanna.capstone.payload.ProductDTO;
import emanuelesanna.capstone.repositories.CategoryRepository;
import emanuelesanna.capstone.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private Cloudinary cloudinary;

    public Product saveProduct(ProductDTO dto) {
        if (productRepository.existsByName(dto.name())) {
            throw new BadRequestException("Esiste già un prodotto con questo nome: " + dto.name());
        }

        String slug = slugify(dto.name());
        if (productRepository.findBySlug(slug).isPresent()) {
            throw new BadRequestException("Un prodotto con il nome " + dto.name() + " esiste già.");
        }
    }

    private String slugify(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        return text.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")

                .replaceAll("\\s+", "-")

                .replaceAll("^-|-$", "");
    }
}
