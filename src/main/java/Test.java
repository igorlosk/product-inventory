import model.Product;
import service.ProductUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Test {
    public static void main(String[] args) {

        Product mouse = new Product(1L, "Mouse", new BigDecimal("100"), 5);
        Product keyboard = new Product(2L, "Keyboard", new BigDecimal("200"), 0);
        Product monitor = new Product(3L, "Monitor", new BigDecimal("500"), 2);
        Product laptop = new Product(4L, "Laptop", new BigDecimal("1000"), 1);
        Product headphones = new Product(5L, "Headphones", new BigDecimal("50"), 3);
        Product adapter = new Product(6L, "Adapter", new BigDecimal("100"), 0);
        Product cable = new Product(7L, "Cable", new BigDecimal("250"), 7);
        Product webcam = new Product(8L, "Webcam", new BigDecimal("200"), 0);

        List<Product> products = new ArrayList<>();
        products.add(mouse);
        products.add(keyboard);
        products.add(monitor);
        products.add(laptop);
        products.add(headphones);
        products.add(adapter);
        products.add(cable);
        products.add(webcam);

        ProductUtils productUtils = new ProductUtils();

        Function<Product, BigDecimal> getPrice = product -> product.getPrice();

        BigDecimal multiplier = new BigDecimal("0.90");
        Function<BigDecimal, BigDecimal> applyDiscount = price -> price.multiply(multiplier);

        Function<BigDecimal, String> formatPrice = price ->
                price.setScale(2, RoundingMode.HALF_UP)
                        .toPlainString() + " RUB";

        Function<Product, String> pipeline1 = getPrice.andThen(applyDiscount).andThen(formatPrice);

        List<String> print1 = productUtils.transformProducts(products, pipeline1);
        System.out.println("Первая цепочка:");
        print1.forEach(System.out::println);

        Function<Product, String> pipeline2 = applyDiscount.compose(getPrice).andThen(formatPrice);

        List<String> print2 = productUtils.transformProducts(products, pipeline2);
        System.out.println("\nВторая цепочка:");
        print2.forEach(System.out::println);

    }
}
