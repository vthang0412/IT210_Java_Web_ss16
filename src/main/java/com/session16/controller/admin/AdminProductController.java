package com.session16.controller.admin;

import com.session16.entity.Product;
import com.session16.service.CategoryService;
import com.session16.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/products/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "admin/products/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Product product,
                       BindingResult bindingResult,
                       @RequestParam(required = false) Long categoryId,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (categoryId == null) {
            model.addAttribute("categoryError", "Vui long chon danh muc");
        }

        if (bindingResult.hasErrors() || categoryId == null) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/products/form";
        }

        product.setCategory(categoryService.findById(categoryId));
        productService.save(product);
        redirectAttributes.addFlashAttribute("success", "Luu san pham thanh cong!");
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Da xoa san pham.");
        return "redirect:/admin/products";
    }
}
