package emanuelesanna.capstone.controllers;

import emanuelesanna.capstone.repositories.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ProductImageController {

    @Autowired
    private ProductImageRepository productImageRepository;
    
    @DeleteMapping("/product-images/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID id) {
        if (productImageRepository.existsById(id)) {
            productImageRepository.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }
}
