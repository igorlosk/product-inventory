package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                5
        );
    }

    @Test
    void shouldChangePrice() {

        product.changePrice(new BigDecimal("150"));

        assertEquals(
                new BigDecimal("150"),
                product.getPrice()
        );
    }

    @Test
    void shouldRejectNegativePrice() {

        assertThrows(
                IllegalArgumentException.class,
                () -> product.changePrice(new BigDecimal("-50"))
        );

        assertEquals(
                new BigDecimal("100"),
                product.getPrice()
        );
    }

    @Test
    void shouldAddQuantity() {

        product.addQuantity(3);

        assertEquals(
                8,
                product.getQuantity()
        );
    }

    @Test
    void shouldRejectZeroQuantity() {

        assertThrows(
                IllegalArgumentException.class,
                () -> product.addQuantity(0)
        );

        assertEquals(
                5,
                product.getQuantity()
        );
    }

    @Test
    void shouldReduceQuantity() {
        Product product = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                10
        );

        product.reduceQuantity(4);

        assertEquals(
                6,
                product.getQuantity()
        );
    }

    @Test
    void shouldRejectInsufficientStock() {

        assertThrows(
                IllegalArgumentException.class,
                () -> product.reduceQuantity(10)
        );

        assertEquals(
                5,
                product.getQuantity()
        );
    }

    @Test
    void shouldRejectQuantityOverflow() {
        Product product = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                Integer.MAX_VALUE
        );

        assertThrows(
                ArithmeticException.class,
                () -> product.addQuantity(1)
        );

        assertEquals(
                Integer.MAX_VALUE,
                product.getQuantity()
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-100, -10, -1, 0})
    void shouldRejectNonPositiveQuantity(int amount) {
        assertThrows(
                IllegalArgumentException.class,
                () -> product.addQuantity(amount));

        assertEquals(5, product.getQuantity())
        ;
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 100})
    void shouldAddPositiveQuantity(int amount) {
        product.addQuantity(amount);

        assertEquals(
                5 + amount,
                product.getQuantity()
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-10, -1, 0, 6, 100})
    void shouldRejectInvalidReduction(int amount) {

        assertThrows(
                IllegalArgumentException.class,
                () -> product.reduceQuantity(amount)
        );

        assertEquals(5, product.getQuantity()
        );

    }

    @ParameterizedTest
    @CsvSource({
            "5, 3, 8",
            "0, 1, 1",
            "10, 15, 25",
            "100, 100, 200"
    })
    void shouldAddQuantityFromCsv(
            int initialQuantity,
            int amount,
            int expectedQuantity
    ) {

        Product product = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                initialQuantity
        );

        product.addQuantity(amount);

        assertEquals(expectedQuantity, product.getQuantity());
    }

    @ParameterizedTest
    @CsvSource({
            "10, 3, 7",
            "5, 5, 0",
            "100, 40, 60",
            "1, 1, 0"
    })
    void shouldReduceQuantityFromCsv(
            int initialQuantity,
            int amount,
            int expectedQuantity
    ){
        Product product = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                initialQuantity
        );

        product.reduceQuantity(amount);

        assertEquals(expectedQuantity, product.getQuantity());
    }

    @ParameterizedTest
    @CsvSource({
            "5, 6",
            "5, 100",
            "0, 1",
            "10, -1",
            "10, 0"
    })
    void shouldRejectInvalidStockReductionFromCsv(
            int initialQuantity,
            int amount
    ){
        Product product = new Product(
                1L,
                "Mouse",
                new BigDecimal("100"),
                initialQuantity
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> product.reduceQuantity(amount)
        );

        assertEquals(
                initialQuantity,
                product.getQuantity()
        );

    }
}
