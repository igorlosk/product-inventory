package service;

import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class ProductUtilsTest {

    private List<Product> products;
    private ProductUtils productUtils;
    private Product mouse;
    private Product keyboard;
    private Product monitor;
    private Product laptop;


    @BeforeEach
    void setUp() {
        products = new ArrayList<>();
        productUtils = new ProductUtils();
        mouse = new Product(1L, "Mouse", new BigDecimal("2"), 5);
        keyboard = new Product(2L, "Keyboard", new BigDecimal("15"), 0);
        monitor = new Product(3L, "Monitor", new BigDecimal("120"), 2);
        laptop = new Product(4L, "Laptop", new BigDecimal("300"), 0);
    }

    @Test
    void shouldRemoveOutOfStockProducts() {
        products.add(mouse);
        products.add(keyboard);
        products.add(monitor);

        productUtils.removeOutOfStockProducts(products);

        assertEquals(2, products.size());
        assertTrue(products.contains(mouse));
        assertTrue(products.contains(monitor));
        assertFalse(products.contains(keyboard));
    }

    @Test
    void shouldKeepAllProductsWhenStockIsPositive() {
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
        products.add(saved1);
        products.add(saved2);

        productUtils.removeOutOfStockProducts(products);

        assertEquals(2, products.size());
        assertTrue(products.contains(saved1));
        assertTrue(products.contains(saved2));
    }

    @Test
    void shouldRemoveAllProductsWhenStockIsZero() {
        Product saved1 = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                0
        );

        Product saved2 = new Product(
                2L,
                "Mouse",
                new BigDecimal("100"),
                0
        );
        products.add(saved1);
        products.add(saved2);

        productUtils.removeOutOfStockProducts(products);

        assertEquals(0, products.size());
        assertFalse(products.contains(saved1));
        assertFalse(products.contains(saved2));
    }

    @Test
    void shouldKeepEmptyListEmpty() {

        productUtils.removeOutOfStockProducts(products);

        assertEquals(0, products.size());
        assertTrue(products.isEmpty());

    }

    @Test
    void shouldAddReplacementAfterOutOfStockProduct() {
        products.add(mouse);
        products.add(keyboard);

        productUtils.addReplacementAfterOutOfStock(products);

        assertEquals(3, products.size());
        assertEquals(keyboard.getId() + 1000, products.get(2).getId());
        assertEquals("Replacement for " + keyboard.getName(), products.get(2).getName());
        assertEquals(BigDecimal.ZERO, products.get(2).getPrice());
        assertEquals(1, products.get(2).getQuantity());
    }

    @Test
    void shouldAddReplacementsForConsecutiveOutOfStockProducts() {
        products.add(keyboard);
        products.add(laptop);

        productUtils.addReplacementAfterOutOfStock(products);

        assertEquals(4, products.size());

        assertEquals(keyboard.getId() + 1000, products.get(1).getId());
        assertEquals("Replacement for " + keyboard.getName(), products.get(1).getName());
        assertEquals(BigDecimal.ZERO, products.get(1).getPrice());
        assertEquals(1, products.get(1).getQuantity());

        assertEquals(laptop.getId() + 1000, products.get(3).getId());
        assertEquals("Replacement for " + laptop.getName(), products.get(3).getName());
        assertEquals(BigDecimal.ZERO, products.get(3).getPrice());
        assertEquals(1, products.get(3).getQuantity());
    }

    @Test
    void shouldNotAddReplacementsWhenAllProductsInStock() {
        products.add(mouse);
        products.add(monitor);

        productUtils.addReplacementAfterOutOfStock(products);

        assertEquals(2, products.size());

        assertSame(mouse, products.get(0));
        assertSame(monitor, products.get(1));
    }

    @Test
    void shouldFilterProductsByPositiveStock() {
        products.add(mouse);
        products.add(keyboard);
        products.add(monitor);
        products.add(laptop);

        Predicate<Product> positiveStock = product -> product.getQuantity() > 0;
        List<Product> sortedList = productUtils.filterProducts(products, positiveStock);

        assertEquals(2, sortedList.size());
        assertSame(mouse, sortedList.get(0));
        assertSame(monitor, sortedList.get(1));
    }

    @Test
    void shouldFilterProductsByPriceAndStock() {
        products.add(mouse);
        products.add(keyboard);
        products.add(monitor);
        products.add(laptop);

        Predicate<Product> positiveStock = product ->
                product.getQuantity() > 0;
        Predicate<Product> priceFilter = product ->
                product.getPrice().compareTo(new BigDecimal("100")) > 0;

        List<Product> sortedList = productUtils.filterProducts(products, positiveStock.and(priceFilter));
        assertEquals(1, sortedList.size());
        assertEquals(new BigDecimal("120"), sortedList.getFirst().getPrice());
        assertEquals(2, sortedList.getFirst().getQuantity());
    }

    @Test
    void shouldReturnEmptyListWhenNoProductsMatch() {
        products.add(mouse);
        products.add(keyboard);
        products.add(monitor);
        products.add(laptop);

        Predicate<Product> positiveStock = product ->
                product.getQuantity() > 0;
        Predicate<Product> priceFilter = product ->
                product.getPrice().compareTo(new BigDecimal("200")) > 0;

        List<Product> sortedList = productUtils.filterProducts(products, positiveStock.and(priceFilter));

        assertEquals(0, sortedList.size());
    }

    @Test
    void shouldNotModifyOriginalListAfterFiltering() {
        products.add(mouse);
        products.add(keyboard);
        products.add(monitor);
        products.add(laptop);

        List<Product> original = new ArrayList<>(products);
        List<Product> filtered = productUtils.filterProducts(products, p -> p.getQuantity() > 0);

        assertEquals(original, products);
        assertNotSame(products, filtered);
        assertEquals(new BigDecimal("2"), mouse.getPrice());
    }

    @Test
    void shouldIncreaseStockAndThenPrintProducts() {
        products.add(mouse);
        products.add(keyboard);
        products.add(monitor);

        Consumer<Product> increaseStock =
                product -> product.addQuantity(2);

        Consumer<Product> printProduct =
                product -> System.out.println(
                        product.getName() + " " + product.getQuantity()
                );

        Consumer<Product> combined =
                increaseStock.andThen(printProduct);

        productUtils.processProducts(products, combined);

        assertEquals(7, mouse.getQuantity());
        assertEquals(2, keyboard.getQuantity());
        assertEquals(4, monitor.getQuantity());

    }

    @Test
    void shouldExecuteConsumersInCorrectOrder() {
        products.add(mouse);

        List<String> events = new ArrayList<>();

        Consumer<Product> first = product ->
                events.add("first");

        Consumer<Product> second = product ->
                events.add("second");

        Consumer<Product> combined = first.andThen(second);

        productUtils.processProducts(products, combined);
        assertEquals(List.of("first", "second"), events);
    }

    @Test
    void shouldSkipSecondConsumerWhenFirstThrows() {
        products.add(mouse);

        List<String> events = new ArrayList<>();

        Consumer<Product> first = product -> product.reduceQuantity(10);

        Consumer<Product> second = product ->
                events.add("second");

        Consumer<Product> combined = first.andThen(second);

        assertThrows(
                IllegalArgumentException.class, () ->
                        productUtils.processProducts(products, combined));

        assertTrue(events.isEmpty());
        assertEquals(5, mouse.getQuantity());
    }

    @Test
    void shouldApplyDiscountAndFormatPrice() {
        Product saved1 = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                0
        );

        products.add(saved1);

        BigDecimal multiplier = new BigDecimal("0.90");
        Function<Product, BigDecimal> applyDiscount = product ->
                product.getPrice().multiply(multiplier);

        Function<BigDecimal, String> formatPrice = price ->
                price.setScale(2, RoundingMode.HALF_UP)
                        .toPlainString() + " RUB";

        Function<Product, String> pipeline = applyDiscount.andThen(formatPrice);
        List<String> result = productUtils.transformProducts(products, pipeline);

        assertEquals(List.of("90.00 RUB"), result);
        assertEquals(new BigDecimal("100"), saved1.getPrice());
    }

    @Test
    void shouldProduceSameResultWithAndThenAndCompose() {
        Product saved1 = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                0
        );

        products.add(saved1);

        BigDecimal multiplier = new BigDecimal("0.90");
        Function<Product, BigDecimal> applyDiscount = product ->
                product.getPrice().multiply(multiplier);

        Function<BigDecimal, String> formatPrice = price ->
                price.setScale(2, RoundingMode.HALF_UP)
                        .toPlainString() + " RUB";

        Function<Product, String> pipeline1 = applyDiscount.andThen(formatPrice);
        List<String> result1 = productUtils.transformProducts(products, pipeline1);

        Function<Product, String> pipeline2 = formatPrice.compose(applyDiscount);
        List<String> result2 = productUtils.transformProducts(products, pipeline2);

        assertEquals(result1, result2);
        assertEquals(List.of("90.00 RUB"), result1);
        assertEquals(new BigDecimal("100"), saved1.getPrice());
    }
}
