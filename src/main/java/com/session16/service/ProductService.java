package com.session16.service;

import com.session16.entity.Product;
import com.session16.repository.ProductRepository;
import com.session16.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> search(String name, BigDecimal minPrice, BigDecimal maxPrice,
                                Long categoryId, int page, int size) {
        Specification<Product> nameSpec = ProductSpecification.hasName(name);
        Specification<Product> minPriceSpec = ProductSpecification.hasMinPrice(minPrice);
        Specification<Product> maxPriceSpec = ProductSpecification.hasMaxPrice(maxPrice);
        Specification<Product> categorySpec = ProductSpecification.hasCategory(categoryId);

        Specification<Product> spec = Specification.where(nameSpec)
                .and(minPriceSpec)
                .and(maxPriceSpec)
                .and(categorySpec);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return productRepository.findAll(spec, pageable);
    }

    public List<Product> findAll() {
        return productRepository.findAll(Sort.by("id").descending());
    }

    public Product findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new RuntimeException("Khong tim thay san pham id=" + id);
        }
        return product.get();
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
