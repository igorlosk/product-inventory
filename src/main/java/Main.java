import model.Product;
import repository.ProductRepository;
import service.ProductService;

import java.math.BigDecimal;


public class Main {
    public static void main(String[] args) {
        ProductRepository repository = new ProductRepository();
        ProductService service = new ProductService(repository);

        Product mouse = new Product(1L, "Mouse", new BigDecimal("2"), 5);
        Product keyboard = new Product(2L, "Keyboard", new BigDecimal("15"), 10);
        Product monitor = new Product(3L, "Monitor", new BigDecimal("120"), 7);

        service.addProduct(mouse);
        service.addProduct(keyboard);
        service.addProduct(monitor);

        try {
            service.addProduct(mouse);
            System.out.println("ERROR: Duplicate was accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        try {
            service.getProduct(5L);
            System.out.println("ERROR: Missing product was found");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }


        System.out.println("Before updatePrice: " + service.getProduct(1L).getPrice());
        try {
            service.updatePrice(1L, new BigDecimal("-120"));
            System.out.println("ERROR: Negative price was accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("After negative updatePrice: " + service.getProduct(1L).getPrice());

        int before = service.getProduct(1L).getQuantity();

        try {
            service.reduceStock(1L, 10);
            System.out.println("ERROR: Invalid reduction accepted");
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        int after = service.getProduct(1L).getQuantity();

        System.out.println("Quantity before: " + before);
        System.out.println("Quantity after: " + after);


        service.getAllProducts().forEach(System.out::println);

        service.updatePrice(1L, new BigDecimal("120"));

        service.restock(3L, 3);
        service.reduceStock(1L, 4);

        service.getAllProducts().forEach(element ->
                System.out.println("Позиция товара " + element.getId() +
                        ", название: " + element.getName() +
                        ", цена: " + element.getPrice() +
                        ", количество: " + element.getQuantity()));
        System.out.println("Общая стоимость склада: " + service.calculateInventoryValue());


    }
}
