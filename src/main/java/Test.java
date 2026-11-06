import model.Product;
import service.ProductUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Test {
    public static void main(String[] args) {

        Product mouse = new Product(1L, "Mouse", new BigDecimal("2"), 5);
        Product keyboard = new Product(2L, "Keyboard", new BigDecimal("15"), 0);
        Product monitor = new Product(3L, "Monitor", new BigDecimal("120"), 2);
        Product laptop = new Product(4L, "Laptop", new BigDecimal("300"), 0);
        Product headphones = new Product(5L, "Headphones", new BigDecimal("20"), 7);

        List<Product> products = new ArrayList<>();
        products.add(mouse);
        products.add(keyboard);
        products.add(monitor);
        products.add(laptop);

    }
}
