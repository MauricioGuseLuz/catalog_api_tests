package com.catalog.repositories;

import com.catalog.entities.Product;
import org.junit.jupiter.api.Assertions;
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


}
