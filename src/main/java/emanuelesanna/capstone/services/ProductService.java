package emanuelesanna.capstone.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import emanuelesanna.capstone.entities.Category;
import emanuelesanna.capstone.entities.Product;
import emanuelesanna.capstone.entities.ProductImage;
import emanuelesanna.capstone.enums.Availability;
import emanuelesanna.capstone.exceptions.BadRequestException;
import emanuelesanna.capstone.exceptions.NotFoundException;
import emanuelesanna.capstone.payload.ProductDTO;
import emanuelesanna.capstone.repositories.CategoryRepository;
import emanuelesanna.capstone.repositories.ProductImageRepository;
import emanuelesanna.capstone.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ProductService {

    private static final long MAX_SIZE = 5 * 1024 * 1024;
    private static final List<String> ALLOWED_TYPES = List.of("image/png", "image/jpeg");
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ProductImageRepository productImageRepository;

    public Product findById(UUID idProduct) {
        return productRepository.findById(idProduct)
                .orElseThrow(() -> new NotFoundException("Prodotto con ID " + idProduct + " non trovato."));
    }

    public Product findBySlug(String slug) {
        return productRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Prodotto con slug '" + slug + "' non trovato."));
    }

    public Product saveProduct(ProductDTO dto) {
        if (productRepository.existsByName(dto.name())) {
            throw new BadRequestException("Esiste già un prodotto con questo nome: " + dto.name());
        }

        String slug = slugify(dto.name());
        if (productRepository.findBySlug(slug).isPresent()) {
            throw new BadRequestException("Un prodotto con il nome " + dto.name() + " esiste già.");
        }

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new NotFoundException("Categoria con ID " + dto.categoryId() + " non trovata."));

        Product newProduct = new Product();
        newProduct.setName(dto.name());
        newProduct.setDescription(dto.description());
        newProduct.setPrice(dto.price());
        newProduct.setSlug(slug);
        newProduct.setAvailability(dto.availability());
        newProduct.setHighlighted(dto.highlighted());
        newProduct.setMaterials(dto.materials());
        newProduct.setDimension(dto.dimension());
        newProduct.setCategory(category);
        log.info("Prodotto '{}' salvato con successo.", newProduct.getName());
        return productRepository.save(newProduct);
    }

    public Product findByIdAndUpdate(UUID idProduct, ProductDTO dto) {

        Product found = this.findById(idProduct);

        String newSlug = slugify(dto.name());

        productRepository.findBySlug(newSlug).ifPresent(product -> {
            if (!product.getIdProduct().equals(idProduct)) {
                throw new BadRequestException("Un altro prodotto esiste già con un nome simile (slug: " + newSlug + ")");
            }
        });

        productRepository.findByName(dto.name()).ifPresent(product -> {
            if (!product.getIdProduct().equals(idProduct)) {
                throw new BadRequestException("Un altro prodotto esiste già con questo nome esatto: " + dto.name());
            }
        });

        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new NotFoundException("Categoria con ID " + dto.categoryId() + " non trovata."));

        found.setName(dto.name());
        found.setDescription(dto.description());
        found.setPrice(dto.price());
        found.setSlug(newSlug);
        found.setAvailability(dto.availability());
        found.setHighlighted(dto.highlighted());
        found.setMaterials(dto.materials());
        found.setDimension(dto.dimension());
        found.setCategory(category);

        log.info("Prodotto con ID {} aggiornato.", idProduct);
        return productRepository.save(found);
    }

    public void findByIdAndDelete(UUID idProduct) {
        Product found = this.findById(idProduct);
        this.productRepository.delete(found);
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

    public ProductImage uploadImage(UUID idProduct, MultipartFile file) throws IOException {

        if (file.isEmpty()) throw new BadRequestException("File vuoto!");
        if (file.getSize() > MAX_SIZE) throw new BadRequestException("La dimensione del file supera quella massima");
        if (!ALLOWED_TYPES.contains(file.getContentType()))
            throw new BadRequestException("I formati permessi sono png e jpeg!");

        Product product = this.findById(idProduct);

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imageUrl = (String) uploadResult.get("secure_url");

            ProductImage newImage = new ProductImage();
            newImage.setImageUrl(imageUrl);
            newImage.setAltText(product.getName() + " image");

            newImage.setOrder(product.getProductImages().size() + 1);

            newImage.setProduct(product);

            // 7. Salva la NUOVA IMMAGINE
            ProductImage savedImage = this.productImageRepository.save(newImage);

            log.info("Immagine salvata con successo per il prodotto {}. URL: {}", idProduct, imageUrl);

            return savedImage;

        } catch (IOException e) {
            log.error("Errore durante l'upload dell'immagine per il prodotto {}: {}", idProduct, e.getMessage());
            throw new BadRequestException("Errore del servizio di storage durante l'upload dell'immagine.");
        }
    }

    public Page<Product> getProductWithFilter(
            String partialName,
            String partialDescription,
            String material,
            String dimension,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            UUID categoryId,
            Availability availability,
            Boolean highlighted,
            Pageable pageable
    ) {

        // Trovo tutto

        Specification<Product> spec = (root, query, cb) -> cb.conjunction();

        // Costruttore condizionale

        if (partialName != null && !partialName.isEmpty()) {
            spec = spec.and(ProductSpecification.nameContains(partialName));
        }
        if (partialDescription != null && !partialDescription.isEmpty()) {
            spec = spec.and(ProductSpecification.descriptionContains(partialDescription));
        }
        if (material != null && !material.isEmpty()) {
            spec = spec.and(ProductSpecification.hasMaterials(material));
        }
        if (dimension != null && !dimension.isEmpty()) {
            spec = spec.and(ProductSpecification.hasDimension(dimension));
        }
        if (minPrice != null) {
            spec = spec.and(ProductSpecification.findItemsWithAPriceGreaterThenOrEqualTo(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductSpecification.findItemsWithAPriceLessThenOrEqualTo(maxPrice));
        }
        if (categoryId != null) {
            spec = spec.and(ProductSpecification.byCategory(categoryId));
        }
        if (availability != null) {
            spec = spec.and(ProductSpecification.findAvailableItems(availability));
        }
        if (highlighted != null) {
            spec = spec.and(ProductSpecification.findHighlightedItems(highlighted));
        }

        // Query finale

        return productRepository.findAll(spec, pageable);
    }

}
