package com.catalog.repositories;

import com.catalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        //preparar os dados
        long existingId = 1L;
        //executar a ação
        repository.deleteById(existingId);
        //verificar se realmente deletou
        Optional<Product> result = repository.findById(existingId);
        //testa se o objeto está presente
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void testSaveProduct(){
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(5000.00);
        product.setImgUrl("localhost/image");
        product.setDate(Instant.now());

        //salvar o o produto
        Product savedProduct = repository.save(product);

        //Asserts são para certificar se tudo deu certo
        assertThat(savedProduct.getId()).isNotNull();//verifica se é não nulo
        assertThat(savedProduct.getName()).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("")
    public void testUpdateProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(5000.00);
        product.setImgUrl("localhost/image");
        product.setDate(Instant.now());
        Product savedProduct = repository.save(product);
        savedProduct.setName("Updated Test Product");
        savedProduct.setDescription("Updated Description");
        savedProduct.setPrice(5000.00);
        savedProduct.setImgUrl("localhost/image");
        savedProduct.setDate(Instant.now());
        repository.save(savedProduct);
        Product updatedProduct = repository.findById(savedProduct.getId()).get();
        updatedProduct.setName("Updated Test Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(5000.00);
        updatedProduct.setImgUrl("localhost/image");
        updatedProduct.setDate(Instant.now());
        repository.save(updatedProduct);
        Optional<Product> result = repository.findById(savedProduct.getId());
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Updated Test Product", result.get().getName());
    }

    @Test
    public void testFindAllProducts() {
        long existingId = 1L;
        Optional<Product> productOptional = repository.findById(existingId);
        Assertions.assertTrue(productOptional.isPresent());



    }


}
