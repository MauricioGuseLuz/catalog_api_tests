package com.catalog.controller;

import com.catalog.controllers.CategoryController;
import com.catalog.dto.CategoryDTO;
import com.catalog.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

@WebMvcTest(CategoryController.class)
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        // Inicializa um CategoryDTO para ser usado nos testes
        categoryDTO = new CategoryDTO(1L, "Test Category");
    }

    @Test
    void findAllShouldReturnPage() throws Exception {
        // Cria uma página com uma lista de categorias contendo apenas um item
        PageImpl<CategoryDTO> page = new PageImpl<>(Collections.singletonList(categoryDTO));

        // Moca o serviço para retornar a página criada
        when(categoryService.findAllPaged(any(Pageable.class))).thenReturn(page);

        // Realiza a requisição GET e valida o resultado
        mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value(categoryDTO.getName()));
    }

    @Test
    void findByIdShouldReturnCategory() throws Exception {
        // Moca o serviço para retornar a categoria com id 1L
        when(categoryService.findById(eq(1L))).thenReturn(categoryDTO);

        // Realiza a requisição GET e valida o resultado
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(categoryDTO.getName()));
    }

    @Test
    void insertShouldReturnCategoryDTOCreated() throws Exception {
        // Moca o serviço para retornar a categoria criada
        when(categoryService.insert(any(CategoryDTO.class))).thenReturn(categoryDTO);

        // Realiza a requisição POST e valida o resultado
        mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                        .content(objectMapper.writeValueAsString(categoryDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(categoryDTO.getName()));
    }

    @Test
    void updateShouldReturnCategoryDTO() throws Exception {
        // Moca o serviço para retornar a categoria atualizada
        when(categoryService.update(eq(1L), any(CategoryDTO.class))).thenReturn(categoryDTO);

        // Realiza a requisição PUT e valida o resultado
        mockMvc.perform(MockMvcRequestBuilders.put("/categories/1")
                        .content(objectMapper.writeValueAsString(categoryDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(categoryDTO.getName()));
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        // Moca o serviço para não fazer nada ao deletar a categoria
        doNothing().when(categoryService).delete(eq(1L));

        // Realiza a requisição DELETE e valida o resultado
        mockMvc.perform(MockMvcRequestBuilders.delete("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
