package emanuelesanna.capstone.services;

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

import java.util.UUID;

@Service
@Slf4j
public class CategoryService {
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
        newCategory.setCoverImageUrl(payload.coverImageUrl());

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

