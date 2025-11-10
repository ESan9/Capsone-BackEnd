package emanuelesanna.capstone.controllers;

import emanuelesanna.capstone.entities.Category;
import emanuelesanna.capstone.exceptions.ValidationException;
import emanuelesanna.capstone.payload.NewCategoryDTO;
import emanuelesanna.capstone.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 1. READ ALL (Accessibile a tutti)

    // GET http://localhost:3001/category 200 OK

    @GetMapping
    @PreAuthorize("permitAll()")
    public Page<Category> findAll(
            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return this.categoryService.getAllCategories(pageable);
    }

    //    http://localhost:3001/category/{slug} 200 OK

    @GetMapping("/{slug}")
    @PreAuthorize("permitAll()")
    public Category findBySlug(@PathVariable String slug) {
        return this.categoryService.findBySlug(slug);
    }

    // 2. CREATE (Solo Admin)

    // POST http://localhost:3001/category (+ payload) 201 CREATED

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Category createCategory(@RequestBody @Validated NewCategoryDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.categoryService.saveCategory(payload);
    }

    // 3. UPDATE (Solo Admin)

    // PUT http://localhost:3001/category/{categoryId} 200 OK

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Category updateCategory(@PathVariable UUID categoryId,
                                   @RequestBody @Validated NewCategoryDTO payload,
                                   BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.categoryService.findByIdAndUpdate(categoryId, payload);
    }

    // 4. DELETE (Solo Admin)

    // DELETE http://localhost:3001/category/{categoryId} 204 NC

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteCategory(@PathVariable UUID categoryId) {
        this.categoryService.findByIdAndDelete(categoryId);
    }
}
