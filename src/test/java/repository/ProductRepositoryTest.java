package repository;

import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository repository;
    private Product mouse;

    @BeforeEach
    void setUp() {
        repository = new ProductRepository();

        mouse = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                5
        );
    }

    @Test
    void shouldSaveProduct() {
        repository.save(mouse);

        Product saved = repository.findById(1L);

        assertSame(mouse, saved);
    }

    @Test
    void shouldFindProductById() {
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

        repository.save(saved1);
        repository.save(saved2);

        Product found = repository.findById(2L);

        assertSame(saved2, found);
    }

    @Test
    void shouldReturnNullForMissingProduct() {

        Product found = repository.findById(1L);

        assertNull(found);
    }

    @Test
    void shouldRejectDuplicateId() {
        repository.save(mouse);

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(mouse)
        );

        assertSame(mouse, repository.findById(1L));
    }

    @Test
    void shouldRejectNullProduct() {
        assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(null)
        );
    }

    @Test
    void shouldReturnAllProducts(){
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

        repository.save(saved1);
        repository.save(saved2);
        repository.save(saved3);

        List<Product> found = repository.findAll();

        for (Product product : found) {
            if (product.getId().equals(1L)) {
                assertSame(saved1, product);
            }
        }

        for (Product product : found) {
            if (product.getId().equals(2L)) {
                assertSame(saved2, product);
            }
        }

        for (Product product : found) {
            if (product.getId().equals(3L)) {
                assertSame(saved3, product);
            }
        }

        assertEquals(3, found.size());
    }

    @Test
    void shouldReturnEmptyListWhenRepositoryIsEmpty(){
        List<Product> found = repository.findAll();

        assertEquals(0, found.size());
        assertTrue(found.isEmpty());
    }

    @Test
    void shouldReturnIndependentList(){
        repository.save(mouse);

        List<Product> found = repository.findAll();

        found.clear();

        Product byId = repository.findById(1L);

        assertSame(byId, mouse);
    }

    @Test
    void shouldReturnReferencesToStoredProducts(){
        repository.save(mouse);

        List<Product> found = repository.findAll();

        for (Product product : found) {
            if (product.getId().equals(1L)) {
                product.changePrice(new BigDecimal("200"));
            }
        }

        Product byId = repository.findById(1L);

        assertEquals(mouse.getPrice(), byId.getPrice());
    }

    @Test
    void shouldCheckProductExistence(){
        repository.save(mouse);

        assertTrue(repository.existsById(1L));
        assertFalse(repository.existsById(2L));
    }
}
