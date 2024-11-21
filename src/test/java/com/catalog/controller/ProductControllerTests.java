package com.catalog.controller;

import com.catalog.controllers.ProductController;
import com.catalog.dto.ProductDTO;
import com.catalog.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        // Inicializa um ProductDTO para ser usado nos testes
        productDTO = new ProductDTO(1L, "Test Product", 100.0);
    }

    @Test
    void findAllShouldReturnPage() throws Exception {
        // Cria uma página com uma lista de produtos contendo apenas um item
        PageImpl<ProductDTO> page = new PageImpl<>(Collections.singletonList(productDTO));

        // Moca o serviço para retornar a página criada
        when(productService.findAllPaged(any(Pageable.class))).thenReturn(page);

        // Realiza a requisição GET e valida o resultado
        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(productDTO.getName()));
    }

    @Test
    void findByIdShouldReturnProduct() throws Exception {
        // Moca o serviço para retornar o produto com id 1L
        when(productService.findById(eq(1L))).thenReturn(productDTO);

        // Realiza a requisição GET e valida o resultado
        mockMvc.perform(MockMvcRequestBuilders.get("/products/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(productDTO.getName()));
    }

    @Test
    void insertShouldReturnProductDTOCreated() throws Exception {
        // Moca o serviço para retornar o produto criado
        when(productService.insert(any(ProductDTO.class))).thenReturn(productDTO);

        // Realiza a requisição POST e valida o resultado
        mockMvc.perform(MockMvcRequestBuilders.post("/products")
                        .content(objectMapper.writeValueAsString(productDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(productDTO.getName()));
    }

    @Test
    void updateShouldReturnProductDTO() throws Exception {
        // Moca o serviço para retornar o produto atualizado
        when(productService.update(eq(1L), any(ProductDTO.class))).thenReturn(productDTO);

        // Realiza a requisição PUT e valida o resultado
        mockMvc.perform(MockMvcRequestBuilders.put("/products/1")
                        .content(objectMapper.writeValueAsString(productDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(productDTO.getName()));
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        // Moca o serviço para não fazer nada ao deletar o produto
        doNothing().when(productService).delete(eq(1L));

        // Realiza a requisição DELETE e valida o resultado
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
