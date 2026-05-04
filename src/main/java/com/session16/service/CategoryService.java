package com.session16.service;

import com.session16.entity.Category;
import com.session16.repository.CategoryRepository;
import com.session16.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new RuntimeException("Khong tim thay danh muc id=" + id);
        }
        return category.get();
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public String delete(Long id) {
        boolean hasProduct = productRepository.existsByCategoryId(id);
        if (hasProduct) {
            return "Khong the xoa danh muc nay vi dang co san pham thuoc danh muc.";
        }

        categoryRepository.deleteById(id);
        return null;
    }
}
