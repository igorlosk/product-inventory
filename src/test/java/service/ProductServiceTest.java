package service;

import model.Product;
import repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductRepository repository;
    private ProductService service;
    private Product mouse;

    @BeforeEach
    void setUp() {
        repository = new ProductRepository();
        service = new ProductService(repository);

        mouse = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                5
        );
    }

    @Test
    void shouldAddProduct() {
        service.addProduct(mouse);
        Product found = repository.findById(mouse.getId());
        assertSame(mouse, found);
    }

    @Test
    void shouldGetProductById() {
        service.addProduct(mouse);
        Product found = service.getProduct(mouse.getId());
        assertSame(mouse, found);
    }

    @Test
    void shouldRejectMissingProduct() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.getProduct(99L));
    }

    @Test
    void shouldUpdateProductPrice() {
        service.addProduct(mouse);
        service.updatePrice(mouse.getId(), new BigDecimal("250"));
        Product found = service.getProduct(mouse.getId());
        assertEquals(new BigDecimal("250"), found.getPrice());
    }

    @Test
    void shouldRejectNegativePriceUpdate() {
        service.addProduct(mouse);
        assertThrows(
                IllegalArgumentException.class,
                () -> service.updatePrice(mouse.getId(), new BigDecimal("-50"))
        );
        Product found = service.getProduct(mouse.getId());
        assertEquals(new BigDecimal("100"), found.getPrice());

    }

    @Test
    void shouldGetAllProducts() {
        Product saved1 = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                5
        );

        Product saved2 = new Product(
                2L,
                "Mouse",
                new BigDecimal("100"),
                5
        );

        Product saved3 = new Product(
                3L,
                "Mouse",
                new BigDecimal("100"),
                5
        );

        service.addProduct(saved1);
        service.addProduct(saved2);
        service.addProduct(saved3);
        List<Product> found = service.getAllProducts();

        assertTrue(found.contains(saved1));
        assertTrue(found.contains(saved2));
        assertTrue(found.contains(saved3));
        assertEquals(3, found.size());
    }

    @Test
    void shouldRestockProduct() {
        service.addProduct(mouse);
        service.restock(1L, 10);
        Product found = service.getProduct(mouse.getId());
        assertEquals(15, found.getQuantity());
    }

    @Test
    void shouldReduceProductStock() {
        service.addProduct(mouse);
        service.reduceStock(1L, 3);
        Product found = service.getProduct(mouse.getId());
        assertEquals(2, found.getQuantity());
    }

    @Test
    void shouldRejectInsufficientStock() {
        service.addProduct(mouse);
        assertThrows(
                IllegalArgumentException.class,
                () -> service.reduceStock(1L, 10)
        );
        Product found = service.getProduct(mouse.getId());
        assertEquals(5, found.getQuantity());
    }

    @Test
    void shouldCalculateInventoryValue() {
        Product saved1 = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                5
        );

        Product saved2 = new Product(
                2L,
                "Keyboard",
                new BigDecimal("200"),
                3
        );

        Product saved3 = new Product(
                3L,
                "Monitor",
                new BigDecimal("500"),
                2
        );

        service.addProduct(saved1);
        service.addProduct(saved2);
        service.addProduct(saved3);

        BigDecimal actualInventoryValue = service.calculateInventoryValue();

        assertEquals(new BigDecimal("2100"), actualInventoryValue);
    }

    @Test
    void shouldReturnZeroInventoryValueWhenEmpty() {
        assertEquals(0, BigDecimal.ZERO.compareTo(service.calculateInventoryValue()));
    }
}
