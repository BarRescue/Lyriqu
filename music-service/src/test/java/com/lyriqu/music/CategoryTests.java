package com.lyriqu.music;

import com.lyriqu.music.entity.Category;
import com.lyriqu.music.logic.CategoryLogic;
import com.lyriqu.music.models.CategoryDTO;
import com.lyriqu.music.rabbitmq.logic.SendLogic;
import com.lyriqu.music.rabbitmq.models.ReturnMessage;
import com.lyriqu.music.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryTests {

    @InjectMocks
    private CategoryLogic categoryLogic;

    @Mock
    private CategoryService categoryService;

    @Mock
    private SendLogic sendLogic;

    UUID categoryId = UUID.fromString("123e4567-e89b-42d3-a456-556642440010");

    @Test
    void createCategorySuccess() throws Exception {
        CategoryDTO dto = this.getCategoryDTO();

        lenient().when(sendLogic.createCategoryQueue(dto.getFile())).thenReturn(new ReturnMessage(true, "test"));

        lenient().when(categoryService.createOrUpdate(any(Category.class))).thenReturn(this.getCategory());

        assertEquals("Chill", this.categoryLogic.createCategory(dto).getName());
    }

    @Test
    void createCategoryInvalid() throws IOException {
        CategoryDTO dto = this.getCategoryDTO();

        lenient().when(sendLogic.createCategoryQueue(dto.getFile())).thenReturn(new ReturnMessage(false, "Invalid Data"));

        assertThrows(Exception.class, () -> {
            this.categoryLogic.createCategory(dto);
        });
    }

    @Test
    void getCategorySuccess() throws Exception {
        lenient().when(categoryService.getCategory(categoryId.toString())).thenReturn(Optional.of(this.getCategory()));

        assertEquals("Chill", this.categoryLogic.getCategory(categoryId.toString()).getName());
    }

    @Test
    void getCategoryNotFound() {
        lenient().when(categoryService.getCategory(categoryId.toString())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            this.categoryLogic.getCategory(categoryId.toString());
        });
    }

    @Test
    void getAllCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(this.getCategory());

        lenient().when(categoryService.getCategories()).thenReturn(categories);

        assertEquals(1, this.categoryLogic.getCategories().size());
    }

    private Category getCategory() {
        Category category = new Category();
        category.setName("Chill");
        category.setId(categoryId);
        category.setDescription("Chill songs");

        return category;
    }

    private CategoryDTO getCategoryDTO() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("R&B");
        dto.setDescription("R&B Songs");
        dto.setFile(new MockMultipartFile("image", "r&bsongs.jpeg", "image/jpeg", "some other type".getBytes()));

        return dto;
    }
}