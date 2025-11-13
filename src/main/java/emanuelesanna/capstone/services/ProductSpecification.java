package emanuelesanna.capstone.services;

import emanuelesanna.capstone.entities.Category;
import emanuelesanna.capstone.entities.Product;
import emanuelesanna.capstone.enums.Availability;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductSpecification {

//    Filtro per contenuto nome

    public static Specification<Product> nameContains(String partialName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + partialName.toLowerCase() + "%");
    }

//    Filtro per contenuto descrizione

    public static Specification<Product> descriptionContains(String partialdescription) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + partialdescription.toLowerCase() + "%");
    }

//    Filtro materiali

    public static Specification<Product> hasMaterials(String material) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("materials")), "%" + material.toLowerCase() + "%");
    }

//    Filtro dimensioni

    public static Specification<Product> hasDimension(String dimension) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("dimension")), "%" + dimension.toLowerCase() + "%");
    }

//    Filtri prezzi

    public static Specification<Product> findItemsWithAPriceGreaterThenOrEqualTo(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> findItemsWithAPriceLessThenOrEqualTo(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

//    Filtro categoria

    public static Specification<Product> byCategory(UUID categoryId) {
        return (root, query, criteriaBuilder) -> {
            // Join" sulla relazione "category" del prodotto
            Join<Product, Category> categoryJoin = root.join("category");

            return criteriaBuilder.equal(categoryJoin.get("idCategory"), categoryId);
        };
    }

//    Filtro disponibilità

    public static Specification<Product> findAvailableItems(Availability availability) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal((root.get("availability")), availability);
    }


//    Filtro in evidenza

    public static Specification<Product> findHighlightedItems(boolean highlighted) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("highlighted"), highlighted);
    }


}
