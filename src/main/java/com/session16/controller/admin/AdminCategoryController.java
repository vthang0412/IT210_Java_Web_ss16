package com.session16.controller.admin;

import com.session16.entity.Category;
import com.session16.service.CategoryService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("category", new Category());
        return "admin/categories/list";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute Category category,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/categories/list";
        }

        categoryService.save(category);
        redirectAttributes.addFlashAttribute("success", "Luu danh muc thanh cong!");
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "admin/categories/list";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String error = categoryService.delete(id);
        if (error != null) {
            redirectAttributes.addFlashAttribute("error", error);
        } else {
            redirectAttributes.addFlashAttribute("success", "Da xoa danh muc.");
        }
        return "redirect:/admin/categories";
    }
}
