package com.catalog.repositories;


import com.catalog.entities.Category;
import com.catalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        long existingId = 1L;
        categoryRepository.deleteById(existingId);
        Optional<Category> category = categoryRepository.findById(existingId);
        Assertions.assertFalse(category.isPresent());
    }

    @Test
    public void testSaveCategory() {
        Category category = new Category();
        category.setName("Test Category");
        Category savedCategory = categoryRepository.save(category);
        assertThat(savedCategory.getId()).isNotNull();//verifica se é não nulo
        assertThat(savedCategory.getName()).isEqualTo("Test Category");
    }

    @Test
    @DisplayName("")
    public void testUpdateCategory() {
        Category category = new Category();
        category.setName("Test Category");
        Category savedCategory = categoryRepository.save(category);
        savedCategory.setName("Updated Category");
        Category updatedCategory = categoryRepository.save(savedCategory);
        assertThat(updatedCategory.getName()).isEqualTo("Updated Category");
    }

    @Test
    public void testFindAllCategories() {
        long existingId = 1L;
        Optional<Category> categoryOptional = categoryRepository.findById(existingId);
        Assertions.assertTrue(categoryOptional.isPresent());


    }
}
