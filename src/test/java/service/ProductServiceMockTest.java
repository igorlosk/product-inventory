package service;

import model.Product;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceMockTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private Product mouse;

    @BeforeEach
    void setUp() {

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

        assertEquals(2, actualProducts.size());
        assertTrue(actualProducts.contains(mouse));
        assertTrue(actualProducts.contains(keyboard));
        verify(repository).findAll();
    }

    @Test
    void shouldCallFindByIdTwice() {
        when(repository.findById(1L)).thenReturn(mouse);
        service.getProduct(1L);
        service.getProduct(1L);
        verify(repository, times(2)).findById(1L);
    }

    @Test
    void shouldNotSaveProductWhenReading() {
        when(repository.findById(1L)).thenReturn(mouse);
        service.getProduct(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldSaveAnyProduct() {
        service.addProduct(mouse);
        verify(repository).save(any(Product.class));
        verify(repository).save(mouse);
    }

    @Test
    void shouldFindProductWithExactId() {
        when(repository.findById(1L)).thenReturn(mouse);
        service.getProduct(1L);
        verify(repository).findById(eq(1L));
    }

    @Test
    void shouldCaptureSavedProduct() {
        service.addProduct(mouse);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repository).save(captor.capture());

        Product captured = captor.getValue();

        assertSame(mouse, captured);
        assertEquals(1L, captured.getId());
        assertEquals("Mouse", captured.getName());
        assertEquals(new BigDecimal("100"), captured.getPrice());
        assertEquals(5, captured.getQuantity());

    }

    @Test
    void shouldCaptureTwoSavedProducts() {
        Product keyboard = new Product(
                2L,
                "Keyboard",
                new BigDecimal("200"),
                3
        );
        service.addProduct(mouse);
        service.addProduct(keyboard);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repository, times(2)).save(captor.capture());
        List<Product> capturedProducts = captor.getAllValues();
        assertEquals(2, capturedProducts.size());
        assertEquals(1L, capturedProducts.get(0).getId());
        assertEquals(2L, capturedProducts.get(1).getId());
    }

    @Test
    void shouldCaptureExactProductReference() {
        service.addProduct(mouse);
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repository).save(captor.capture());
        assertSame(mouse, captor.getValue());
    }

    @Test
    void shouldPropagateRepositoryException() {

        // Arrange — настраиваем mock
        doThrow(new IllegalArgumentException("Duplicate product"))
                .when(repository)
                .save(mouse);

        // Act + Assert — ожидаем исключение
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.addProduct(mouse)
        );

        // Проверяем сообщение исключения
        assertEquals("Duplicate product", exception.getMessage());

        // Проверяем обращение к репозиторию
        verify(repository, times(1)).save(mouse);
    }

    @Test
    void shouldNotChangePriceWhenProductMissing() {
        // Arrange
        when(repository.findById(99L)).thenReturn(null);

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> service.updatePrice(99L, new BigDecimal("250"))
        );

        assertEquals(
                new BigDecimal("100"),
                mouse.getPrice()
        );

        verify(repository).findById(99L);
    }
}
