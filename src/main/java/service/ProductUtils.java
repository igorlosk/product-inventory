package service;

import model.Product;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ProductUtils {

    public void removeOutOfStockProducts(List<Product> products) {

        Iterator<Product> iterator = products.iterator();

        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getQuantity() == 0) {
                iterator.remove();
            }
        }
    }

    public static void printProductsInReverse(List<Product> products) {

        ListIterator<Product> iterator = products.listIterator(products.size());
        while (iterator.hasPrevious()) {
            Product product = iterator.previous();
            System.out.println(product.getName());
        }
    }

    public static void replaceOutOfStockProducts(List<Product> products) {

        ListIterator<Product> iterator = products.listIterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getQuantity() == 0) {
                iterator.set(new Product(product.getId(), "Out of stock", BigDecimal.ZERO, 0));
            }
        }
    }

    public void addReplacementAfterOutOfStock(List<Product> products) {

        ListIterator<Product> iterator = products.listIterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getQuantity() == 0) {
                iterator.add(new Product(
                        product.getId() + 1000,
                        "Replacement for " + product.getName(),
                        BigDecimal.ZERO,
                        1));
            }
        }
    }
}
