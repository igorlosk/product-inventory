package service;

import model.Product;

import java.math.BigDecimal;
import java.util.Comparator;
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

    public static void sortProductsByPrice(List<Product> products) {
        Comparator<Product> comparator = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o1.getPrice().compareTo(o2.getPrice());
            }
        };

        products.sort(comparator);
    }

    public static void sortProductsByPriceAndName(List<Product> products) {

        Comparator<Product> comparator = new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                int priceComparison =
                        o1.getPrice().compareTo(o2.getPrice());

                if (priceComparison != 0) {
                    return priceComparison;
                }

                return o1.getName().compareTo(o2.getName());
            }
        };
        products.sort(comparator);
    }

    public static void sortByName(List<Product> products) {
        Comparator<Product> comparator = Comparator.comparing(Product::getName);
        products.sort(comparator);
    }

    public static void sortByPriceDescending(List<Product> products) {
        Comparator<Product> comparator = Comparator.comparing(Product::getPrice).reversed();
        products.sort(comparator);
    }

    public static void sortByQuantityAndPrice(List<Product> products) {
        Comparator<Product> comparator = Comparator.comparingInt(
                Product::getQuantity).reversed().thenComparing(Product::getPrice);
        products.sort(comparator);
    }

    public static void sortByPriceLambda(List<Product> products) {
        Comparator<Product> comparator = (p1, p2) -> p1.getPrice().compareTo(p2.getPrice());
        products.sort(comparator);
    }

    public static void sortByNameLength(List<Product> products) {
        Comparator<Product> comparator = (p1, p2) ->
                Integer.compare(
                        p1.getName().length(),
                        p2.getName().length());
        products.sort(comparator);
    }

    public static void sortByPriceAndNameLambda(List<Product> products) {
        Comparator<Product> comparator = (p1, p2) -> {
            int priceComparison =
                    p1.getPrice().compareTo(p2.getPrice());
            if (priceComparison != 0) {
                return priceComparison;
            }
            return p1.getName().compareTo(p2.getName());
        };

        products.sort(comparator);
    }
}


