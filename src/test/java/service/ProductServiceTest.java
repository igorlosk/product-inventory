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

    @Test
    void shouldCalculateInventoryValueAfterStockChanges() {
        Product keyboard = new Product(
                2L,
                "Keyboard",
                new BigDecimal("200"),
                3
        );
        service.addProduct(mouse);
        service.addProduct(keyboard);

        service.restock(1L, 3);
        service.reduceStock(2L, 2);
        service.updatePrice(mouse.getId(), new BigDecimal("150"));

        Product updatedMouse = service.getProduct(1L);
        Product updatedKeyboard = service.getProduct(2L);

        assertEquals(8, updatedMouse.getQuantity());
        assertEquals(1, updatedKeyboard.getQuantity());
        assertEquals(new BigDecimal("150"), updatedMouse.getPrice());
        assertEquals(new BigDecimal("1400"), service.calculateInventoryValue());
    }

    @Test
    void shouldKeepInventoryUnchangedAfterFailedReduction() {
        Product keyboard = new Product(
                2L,
                "Keyboard",
                new BigDecimal("200"),
                3
        );
        service.addProduct(mouse);
        service.addProduct(keyboard);
        Product updatedMouse = service.getProduct(1L);

        BigDecimal inventoryValueBefore = service.calculateInventoryValue();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.reduceStock(updatedMouse.getId(), 10));

        BigDecimal inventoryValueAfter = service.calculateInventoryValue();

        assertEquals(5, updatedMouse.getQuantity());
        assertEquals(inventoryValueBefore, inventoryValueAfter);

    }

    @Test
    void shouldProcessOrderSuccessfully(){
        Product saved1 = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                10
        );

        Product saved2 = new Product(
                2L,
                "Keyboard",
                new BigDecimal("200"),
                5
        );

        service.addProduct(saved1);
        service.addProduct(saved2);

        service.processOrder(saved1.getId(), 3, saved2.getId(),2);

        Product updatedMouse = service.getProduct(1L);
        Product updatedKeyboard = service.getProduct(2L);

        assertEquals(7, updatedMouse.getQuantity());
        assertEquals(3, updatedKeyboard.getQuantity());

    }

    @Test
    void shouldKeepStockUnchangedWhenOrderFails(){
        Product saved1 = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                10
        );

        Product saved2 = new Product(
                2L,
                "Keyboard",
                new BigDecimal("200"),
                2
        );

        service.addProduct(saved1);
        service.addProduct(saved2);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.processOrder(saved1.getId(), 3, saved2.getId(),5));
        Product updatedMouse = service.getProduct(1L);
        Product updatedKeyboard = service.getProduct(2L);
        assertEquals(10, updatedMouse.getQuantity());
        assertEquals(2, updatedKeyboard.getQuantity());
    }

    @Test
    void shouldRejectOrderWithZeroQuantity(){
        Product saved1 = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                10
        );

        Product saved2 = new Product(
                2L,
                "Keyboard",
                new BigDecimal("200"),
                10
        );

        service.addProduct(saved1);
        service.addProduct(saved2);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.processOrder(saved1.getId(), 3, saved2.getId(),0));

        Product updatedMouse = service.getProduct(1L);
        Product updatedKeyboard = service.getProduct(2L);
        assertEquals(10, updatedMouse.getQuantity());
        assertEquals(10, updatedKeyboard.getQuantity());

    }

    @Test
    void shouldRejectOrderWhenProductMissing(){
        service.addProduct(mouse);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.processOrder(mouse.getId(), 3, 99L,5));

        Product savedMouse = service.getProduct(1L);

        assertEquals(5, savedMouse.getQuantity());

    }
    @Test
    void shouldRejectOrderWithSameProductId(){
        service.addProduct(mouse);
        Product keyboard = new Product(
                1L,
                "Keyboard",
                new BigDecimal("200"),
                3
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.processOrder(mouse.getId(), 3, keyboard.getId(),5));

        Product savedMouse = service.getProduct(1L);
        assertEquals(5, savedMouse.getQuantity());
    }

    @Test
    void shouldRejectOrderWithNullProductId(){
        service.addProduct(mouse);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.processOrder(null,3, mouse.getId(),2));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.processOrder(mouse.getId(),2,null,2));

        Product savedMouse = service.getProduct(1L);
        assertEquals(5, savedMouse.getQuantity());
    }
}
