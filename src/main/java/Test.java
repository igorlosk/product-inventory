import model.Product;
import service.ProductUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;

public class Test {
    public static void main(String[] args) {

        Product mouse = new Product(1L, "Mouse", new BigDecimal("100"), 5);
        Product keyboard = new Product(2L, "Keyboard", new BigDecimal("200"), 3);
        Product monitor = new Product(3L, "Monitor", new BigDecimal("500"), 0);
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

        Predicate<Product> quantityFilter = product -> product.getQuantity() > 0;
        Predicate<Product> priceFilter = product ->
                product.getPrice().compareTo(new BigDecimal("200")) > 0;
        Predicate<Product> letterM = product -> product.getName().startsWith("M");

//        System.out.println("Existing products are:");
//        ProductUtils.filterProducts(products, quantityFilter).forEach(System.out::println);
//
//        System.out.println("\nExisting products filtering by price over 200:");
//        ProductUtils.filterProducts(products, priceFilter.and(quantityFilter)).forEach(System.out::println);
//
//        System.out.println("\nProducts starring M letter:");
//        ProductUtils.filterProducts(products, letterM).forEach(System.out::println);

    }
}
