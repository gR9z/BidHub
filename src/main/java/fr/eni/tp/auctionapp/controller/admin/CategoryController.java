package fr.eni.tp.auctionapp.controller.admin;

import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bll.ItemService;
import fr.eni.tp.auctionapp.bo.Category;
import fr.eni.tp.auctionapp.exceptions.BusinessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class CategoryController {

    CategoryService categoryService;
    ItemService itemService;

    public CategoryController(CategoryService categoryService, ItemService itemService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
    }

    @GetMapping("/categories")
    public String displayAllCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        Map<Integer, Integer> itemCountMap = categoryService.getCountByCategory();

        for (Category category : categories) {
            int categoryId = category.getCategoryId();
            category.setNumberOfItems(itemCountMap.getOrDefault(categoryId, 0));
        }

        model.addAttribute("categories", categories);
        return "admin/category/categories.html";
    }

    @GetMapping("/category/add")
    public String addCategoryForm(Model model) {
        Category category = new Category();

        model.addAttribute("category", category);
        return "admin/category/create-category.html";
    }

    @PostMapping("/category/add")
    public String addCategory(
            @ModelAttribute("category") Category category,
            BindingResult bindingResult
    ) {
        try {
            categoryService.createCategory(category);
            return "redirect:/admin/categories";
        } catch (BusinessException businessException) {
            businessException.getKeys().forEach(key -> {
                ObjectError error = new ObjectError("globalError", key);
                bindingResult.addError(error);
            });
            return "admin/category/add";
        }
    }

    @PostMapping("/categories/{categoryId}/delete")
    public String deleteCategory(@PathVariable int categoryId) {
        // TODO FIX : L'instruction DELETE est en conflit avec la contrainte REFERENCE "items_categories_fk".
        categoryService.removeCategoryById(categoryId);
        return "redirect:/admin/categories";
    }

    @GetMapping("/category/{categoryId}/edit")
    public String displayFormUpdateCategory(@PathVariable int categoryId, Model model) {
        Optional<Category> optionalCategory = categoryService.findCategoryById(categoryId);
        optionalCategory.ifPresent(category -> model.addAttribute("category", category));
        return "admin/category/edit-category.html";
    }

    @PostMapping("/category/{categoryId}/edit")
    public String updateCategory(@PathVariable int categoryId, @ModelAttribute("category") Category category, BindingResult bindingResult) {
        try {
            categoryService.updateCategory(category);
            return "redirect:/admin/categories";
        } catch (BusinessException businessException) {
            businessException.getKeys().forEach(key -> {
                ObjectError error = new ObjectError("globalError", key);
                bindingResult.addError(error);
            });
            return "/category/" + categoryId + "/edit";
        }
    }
}
