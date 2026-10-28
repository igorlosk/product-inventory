package service;

import model.Product;
import repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceMockTest {

    private ProductRepository repository;
    private ProductService service;
    private Product mouse;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        service = new ProductService(repository);

        mouse = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                5
        );
    }

    @Test
    void shouldGetProductById() {
        // Arrange
        when(repository.findById(1L))
                .thenReturn(mouse);

        // Act
        Product found = service.getProduct(1L);

        // Assert
        assertSame(mouse, found);
        verify(repository).findById(1L);
    }

    @Test
    void shouldAddProduct() {
        service.addProduct(mouse);
        verify(repository).save(mouse);
    }

    @Test
    void shouldRejectMissingProduct() {
        when(repository.findById(99L)).thenReturn(null);
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getProduct(99L));
        verify(repository).findById(99L);
    }

    @Test
    void shouldUpdateProductPrice() {
        when(repository.findById(1L)).thenReturn(mouse);
        service.updatePrice(1L, new BigDecimal("250"));
        assertEquals(new BigDecimal("250"), mouse.getPrice());
        verify(repository).findById(1L);
    }

    @Test
    void shouldGetAllProducts() {
        Product keyboard = new Product(
                2L,
                "Keyboard",
                new BigDecimal("200"),
                3
        );

        List<Product> expectedProducts = List.of(mouse, keyboard);

        when(repository.findAll()).thenReturn(expectedProducts);

        List<Product> actualProducts = service.getAllProducts();

        assertEquals(2, expectedProducts.size());
        assertTrue(actualProducts.contains(mouse));
        assertTrue(actualProducts.contains(keyboard));
        verify(repository).findAll();
    }
}
