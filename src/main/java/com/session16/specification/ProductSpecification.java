package com.session16.specification;

import com.session16.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return cb.conjunction();
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasMinPrice(BigDecimal min) {
        return (root, query, cb) -> {
            if (min == null) return cb.conjunction();
            return cb.greaterThanOrEqualTo(root.get("price"), min);
        };
    }

    public static Specification<Product> hasMaxPrice(BigDecimal max) {
        return (root, query, cb) -> {
            if (max == null) return cb.conjunction();
            return cb.lessThanOrEqualTo(root.get("price"), max);
        };
    }

    public static Specification<Product> hasCategory(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) return cb.conjunction();
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }
}
