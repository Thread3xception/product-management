package example.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.task.Application;
import example.task.utils.DatabaseCleaner;
import example.task.model.command.CreateProductCommand;
import example.task.model.dto.ProductDto;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static example.task.utils.JsonUtils.extractAs;
import static example.task.utils.JsonUtils.extractAsList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Very basic integration test base on H2.
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIT {

    private static final String API_PATH = "/api/v1/products";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void clean() throws LiquibaseException {
        databaseCleaner.cleanUp();
    }


    @DisplayName("Should get all products")
    @Test
    void shouldGetAllProducts() throws Exception {
        // given
        // when
        MvcResult mvcResult = mockMvc.perform(get(API_PATH)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        List<ProductDto> products = extractAsList(mvcResult, ProductDto.class);

        assertNotNull(products);
        assertEquals(3, products.size());
        assertEquals("First", products.get(0).getName());
        assertEquals(5000.0, products.get(0).getPrice());
        assertEquals("Secondary", products.get(1).getName());
        assertEquals(10000.0, products.get(1).getPrice());
        assertEquals("Third", products.get(2).getName());
        assertEquals(15000.0, products.get(2).getPrice());
    }

    @DisplayName("Should get only two products")
    @Test
    void shouldGetOnlyTwoProducts() throws Exception {
        // given
        // when
        MvcResult mvcResult = mockMvc.perform(get(API_PATH + "?pageNumber=0&size=2")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        List<ProductDto> products = extractAsList(mvcResult, ProductDto.class);

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("First", products.get(0).getName());
        assertEquals(5000.0, products.get(0).getPrice());
        assertEquals("Secondary", products.get(1).getName());
        assertEquals(10000.0, products.get(1).getPrice());
    }


    @DisplayName("Should get single product")
    @Test
    void shouldGetSingleProduct() throws Exception {
        // given
        Long id = 1L;

        // when
        MvcResult mvcResult = mockMvc.perform(get(API_PATH + "/{productId}", id)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // then
        ProductDto product = extractAs(mvcResult, ProductDto.class);

        assertNotNull(product);
        assertEquals("First", product.getName());
        assertEquals(5000.0, product.getPrice());
    }

    @DisplayName("Should get error when single product not found")
    @Test
    void shouldGetErrorWhenSingleProductNotFound() throws Exception {
        // given
        Long id = 100L;
        // when
        // then
        mockMvc.perform(get(API_PATH + "/{productId}", id)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product with id: %s not found!".formatted(id)));
    }


    @DisplayName("Should add new product")
    @Test
    void shouldAddNewProduct() throws Exception {
        // given
        CreateProductCommand command = CreateProductCommand.builder()
                .name("product-name")
                .price(150.50)
                .build();

        // when
        // then
        mockMvc.perform(post(API_PATH)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("product-name"))
                .andExpect(jsonPath("$.price").value(150.50));
    }

    @DisplayName("Should get error when add new product without name")
    @Test
    void shouldGetErrorAddNewProductWithoutName() throws Exception {
        // given
        CreateProductCommand command = CreateProductCommand.builder()
                .price(150.50)
                .build();

        // when
        // then
        mockMvc.perform(post(API_PATH)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Name may not be blank"));
    }

    @DisplayName("Should delete product")
    @Test
    void shouldDeleteProduct() throws Exception {
        // given
        Long id = 1L;

        // when
        // then
        mockMvc.perform(delete(API_PATH + "/{productId}", id)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        // when
        // then
        mockMvc.perform(get(API_PATH + "/{productId}", id)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}