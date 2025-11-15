package emanuelesanna.capstone.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import emanuelesanna.capstone.entities.Category;
import emanuelesanna.capstone.exceptions.BadRequestException;
import emanuelesanna.capstone.exceptions.NotFoundException;
import emanuelesanna.capstone.payload.NewCategoryDTO;
import emanuelesanna.capstone.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class CategoryService {
    private static final long MAX_SIZE = 5 * 1024 * 1024;
    private static final List<String> ALLOWED_TYPES = List.of("image/png", "image/jpeg");
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public Category findById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Categoria con ID " + categoryId + " non trovata."));
    }

    public Category findBySlug(String slug) {
        return categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Categoria con slug '" + slug + "' non trovata."));
    }

    public Category saveCategory(NewCategoryDTO payload) {
        String slug = slugify(payload.name());
        if (categoryRepository.findBySlug(slug).isPresent()) {
            throw new BadRequestException("Una categoria con il nome " + payload.name() + " esiste già.");
        }
        Category newCategory = new Category();
        newCategory.setName(payload.name());
        newCategory.setDescription(payload.description());
        newCategory.setSlug(slug);
        if (payload.coverImageUrl() == null || payload.coverImageUrl().isEmpty()) {
            newCategory.setCoverImageUrl("https://placehold.co/600x400/E2E8F0/2D3748?text=" + payload.name().replace(" ", "+"));
        } else {
            newCategory.setCoverImageUrl(payload.coverImageUrl());
        }

        log.info("Categoria '{}' salvata con successo.", newCategory.getName());
        return categoryRepository.save(newCategory);
    }

    public Category findByIdAndUpdate(UUID categoryId, NewCategoryDTO payload) {
        Category found = this.findById(categoryId);
        String newSlug = slugify(payload.name());

        categoryRepository.findBySlug(newSlug).ifPresent(category -> {
            if (!category.getIdCategory().equals(categoryId)) {
                throw new BadRequestException("Esiste già un'altra categoria con questo nome");
            }
        });
        found.setName(payload.name());
        found.setDescription(payload.description());
        found.setSlug(newSlug);
        found.setCoverImageUrl(payload.coverImageUrl());
        log.info("Categoria con ID {} aggiornata.", categoryId);
        return categoryRepository.save(found);
    }

    public void findByIdAndDelete(UUID categoryId) {
        Category found = this.findById(categoryId);

        if (found.getProducts() != null && !found.getProducts().isEmpty()) {
            throw new BadRequestException("Impossibile eliminare la categoria con ID " + categoryId + " perché contiene prodotti.");
        }

        log.warn("Categoria '{}' con ID {} eliminata.", found.getName(), categoryId);
        categoryRepository.delete(found);
    }

    public Category uploadCoverImage(UUID idCategory, MultipartFile file) throws IOException {

        if (file.isEmpty()) throw new BadRequestException("File vuoto!");
        if (file.getSize() > MAX_SIZE) throw new BadRequestException("La dimensione del file supera quella massima");
        if (!ALLOWED_TYPES.contains(file.getContentType()))
            throw new BadRequestException("I formati permessi sono png e jpeg!");

        Category category = this.findById(idCategory);

        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");

            category.setCoverImageUrl(imageUrl);

            Category savedCategory = this.categoryRepository.save(category);

            log.info("Immagine di copertina salvata per la categoria {}. URL: {}", idCategory, imageUrl);
            return savedCategory;

        } catch (IOException e) {
            log.error("Errore durante l'upload dell'immagine per la categoria {}: {}", idCategory, e.getMessage());
            throw new BadRequestException("Errore del servizio di storage durante l'upload dell'immagine.");
        }
    }

    // Standardizzare l'url

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

