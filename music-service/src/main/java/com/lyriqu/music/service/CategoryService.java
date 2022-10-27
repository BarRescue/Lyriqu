package com.lyriqu.music.service;

import com.lyriqu.music.entity.Category;
import com.lyriqu.music.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getCategories() {
        return this.repository.findAll();
    }

    public Optional<Category> getCategory(String id) {
        return this.repository.findById(UUID.fromString(id));
    }

    public Category createOrUpdate(Category category) {
        return this.repository.save(category);
    }
}
