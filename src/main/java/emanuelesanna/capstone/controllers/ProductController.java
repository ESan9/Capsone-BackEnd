package emanuelesanna.capstone.controllers;

import emanuelesanna.capstone.entities.Product;
import emanuelesanna.capstone.entities.ProductImage;
import emanuelesanna.capstone.enums.Availability;
import emanuelesanna.capstone.exceptions.BadRequestException;
import emanuelesanna.capstone.exceptions.ValidationException;
import emanuelesanna.capstone.payload.ProductDTO;
import emanuelesanna.capstone.services.ProductService;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    // 1. GET http://localhost:3001/product 200 OK

    @GetMapping
    @PreAuthorize("permitAll()")
    public Page<Product> findAll(

            @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,

            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String dimension,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Availability availability,
            @RequestParam(required = false) Boolean highlighted
    ) {
        return this.productService.getProductWithFilter(
                name, description, material, dimension,
                minPrice, maxPrice, categoryId, availability,
                highlighted,
                pageable
        );
    }

    // 2. READ SINGLE BY SLUG (Pubblico)

    // GET http://localhost:3001/product/borsa-pelle-nera

    @GetMapping("/{slug}")
    @PreAuthorize("permitAll()")
    public Product findBySlug(@PathVariable String slug) {
        return this.productService.findBySlug(slug);
    }

    // 3. CREATE (ADMIN)

    // POST http://localhost:3001/product 201 CREATED

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product createProduct(@RequestBody @Validated ProductDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.productService.saveProduct(payload);
    }

    // 3. Update (ADMIN)

    // POST http://localhost:3001/product/{productId} 200 OK

    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product updateProduct(@PathVariable UUID productId,
                                 @RequestBody @Validated ProductDTO payload,
                                 BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult.getFieldErrors()
                    .stream().map(fieldError -> fieldError.getDefaultMessage()).toList());
        }
        return this.productService.findByIdAndUpdate(productId, payload);
    }

    // 3. Delete (ADMIN)

    // POST http://localhost:3001/product/{productId} 204 NO CONTENT

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteProduct(@PathVariable UUID productId) {
        this.productService.findByIdAndDelete(productId);
    }

    // 3. UploadImage (ADMIN)

    // POST http://localhost:3001/product/{productId}/upload-image 200 OK

    @PostMapping("/{productId}/upload-image")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProductImage uploadImage(@PathVariable UUID productId,
                                    @RequestParam("image") MultipartFile file) {
        try {
            return this.productService.uploadImage(productId, file);
        } catch (IOException e) {
            log.error("Upload dell'immagine fallito per il prodotto {}: {}", productId, e.getMessage());
            throw new BadRequestException("Upload dell'immagine fallito: " + e.getMessage());
        }
    }
}
