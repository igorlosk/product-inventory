import model.Product;
import service.ProductUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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

        ProductUtils productUtils = new ProductUtils();

        Consumer<Product> increaseStock = product -> {
            product.addQuantity(2);
        };

        Consumer<Product> printProduct = product -> {
            System.out.println(product.getName() + " " + product.getQuantity());
        };

        Consumer<Product> combined = increaseStock.andThen(printProduct);
        productUtils.processProducts(products, combined);
    }
}
