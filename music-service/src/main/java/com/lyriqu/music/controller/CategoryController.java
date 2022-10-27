package com.lyriqu.music.controller;

import com.lyriqu.music.entity.Category;
import com.lyriqu.music.logic.CategoryLogic;
import com.lyriqu.music.models.CategoryDTO;
import com.lyriqu.music.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/music/category")
public class CategoryController {

    private CategoryLogic categoryLogic;

    @Autowired
    public CategoryController(CategoryLogic categoryLogic) {
        this.categoryLogic = categoryLogic;
    }

    @GetMapping
    public ResponseEntity getCategories() {
        return ResponseEntity.ok(this.categoryLogic.getCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity getMusicByCategory(@PathVariable String id) {
        try {
            Category category = this.categoryLogic.getCategory(id);

            if(category == null) {
                throw new Exception("Category not found");
            }

            return ResponseEntity.ok().body(category);
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity createCategory(CategoryDTO categoryDTO) {
        try {
            if(categoryDTO.getFile() == null || categoryDTO.getName() == null || categoryDTO.getDescription() == null) {
                throw new Exception("Invalid data");
            }

            return ResponseEntity.ok().body(this.categoryLogic.createCategory(categoryDTO));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
