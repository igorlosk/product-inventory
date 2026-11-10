import model.Product;
import service.ProductUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Test {
    public static void main(String[] args) {

        Product mouse = new Product(1L, "Mouse", new BigDecimal("100"), 5);
        Product keyboard = new Product(2L, "Keyboard", new BigDecimal("200"), 3);
        Product monitor = new Product(3L, "Monitor", new BigDecimal("500"), 5);
        Product laptop = new Product(4L, "Laptop", new BigDecimal("1000"), 1);
        Product headphones = new Product(5L, "Headphones", new BigDecimal("50"), 3);
        Product adapter = new Product(6L, "Adapter", new BigDecimal("100"), 7);
        Product cable = new Product(7L, "Cable", new BigDecimal("100"), 7);
        Product webcam = new Product(8L, "Webcam", new BigDecimal("200"), 7);


        List<Product> products = new ArrayList<>();
        products.add(mouse);
        products.add(keyboard);
        products.add(monitor);
        products.add(laptop);
        products.add(headphones);
        products.add(adapter);
        products.add(cable);
        products.add(webcam);

//        ProductUtils.sortByName(products);
//        System.out.println("sortByName:");
//        for (Product product : products) {
//            System.out.println(product.getName() + " - " + product.getPrice());
//        }
//
//        ProductUtils.sortByPriceDescending(products);
//        System.out.println("\nsortByPriceDescending:");
//        for (Product product : products) {
//            System.out.println(product.getName() + " - " + product.getPrice());
//        }
//
//        ProductUtils.sortByQuantityAndPrice(products);
//        System.out.println("\nsortByQuantityAndPrice:");
//        for (Product product : products) {
//            System.out.println(product.getName() + " - " + product.getPrice() +  " - " + product.getQuantity());
//        }

        ProductUtils.sortByPriceLambda(products);
        System.out.println("\nsortByPriceLambda:");
        for (Product product : products) {
            System.out.println(product.getName() + " - " + product.getPrice());
        }

        ProductUtils.sortByNameLength(products);
        System.out.println("\nsortByNameLength:");
        for (Product product : products) {
            System.out.println(product.getName() + " - " + product.getPrice());
        }

        ProductUtils.sortByPriceAndNameLambda(products);
        System.out.println("\nsortByPriceAndNameLambda:");
        for (Product product : products) {
            System.out.println(product.getName() + " - " + product.getPrice());
        }


    }
}
