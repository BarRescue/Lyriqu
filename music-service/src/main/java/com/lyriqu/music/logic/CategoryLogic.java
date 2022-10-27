package com.lyriqu.music.logic;

import com.lyriqu.music.entity.Category;
import com.lyriqu.music.models.CategoryDTO;
import com.lyriqu.music.rabbitmq.logic.SendLogic;
import com.lyriqu.music.rabbitmq.models.ReturnMessage;
import com.lyriqu.music.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class CategoryLogic {

    private final CategoryService categoryService;
    private final SendLogic sendLogic;

    @Autowired
    public CategoryLogic(CategoryService categoryService, SendLogic sendLogic) {
        this.categoryService = categoryService;
        this.sendLogic = sendLogic;
    }

    public List<Category> getCategories() {
        return this.categoryService.getCategories();
    }

    public Category getCategory(String id) throws Exception {
        Optional<Category> category = this.categoryService.getCategory(id);

        if(category.isPresent()) {
            return category.get();
        }

        throw new Exception("Category couldn't be retrieved.");
    }

    public Category createCategory(CategoryDTO categoryDTO) throws Exception {

        ReturnMessage returnMessage = this.sendLogic.createCategoryQueue(categoryDTO.getFile());

        if(returnMessage.getStatus()) {
            Category category = new Category();
            category.setName(categoryDTO.getName());
            category.setDescription(categoryDTO.getDescription());
            category.setLogoPath(returnMessage.getMessage());

            return this.categoryService.createOrUpdate(category);
        }

        throw new Exception("Could not create category, please try again.");
    }
}
