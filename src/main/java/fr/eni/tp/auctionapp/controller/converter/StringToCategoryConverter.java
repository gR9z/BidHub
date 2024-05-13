package fr.eni.tp.auctionapp.controller.converter;

import fr.eni.tp.auctionapp.bll.CategoryService;
import fr.eni.tp.auctionapp.bo.Category;
import jakarta.annotation.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StringToCategoryConverter implements Converter<String, Category> {

    private final CategoryService categoryService;

    public StringToCategoryConverter(
            CategoryService categoryService
    ){
        this.categoryService = categoryService;
    }

    @Override
    public Category convert(@Nullable String id) {
        if (id == null) {
            return null;
        }
        int categoryId = Integer.parseInt(id);
        Optional<Category> optionalCategory = categoryService.findCategoryById(categoryId);
        return optionalCategory.orElse(null);
    }
}