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

//        System.out.println("Тест № 1 — успешный заказ");
//        Product testMouse1 = new Product(4L, "Mouse", new BigDecimal("2"), 10);
//        Product testKeyboard1 = new Product(5L, "Keyboard", new BigDecimal("15"), 5);
//        service.addProduct(testMouse1);
//        service.addProduct(testKeyboard1);
//        System.out.println("Исходное состояние: " + "Mouse: "
//                + testMouse1.getQuantity() + ", " + "Keyboard: " + testKeyboard1.getQuantity());
//        service.processOrder(testMouse1.getId(), 3, testKeyboard1.getId(), 2);
//        System.out.println("Ожидаемый результат: " + "Mouse: "
//                + testMouse1.getQuantity() + ", " + "Keyboard: " + testKeyboard1.getQuantity());
//        System.out.println("-".repeat(50));
//
//        System.out.println("Тест № 2 — неудачный заказ");
//        Product testMouse2 = new Product(6L, "Mouse", new BigDecimal("2"), 10);
//        Product testKeyboard2 = new Product(7L, "Keyboard", new BigDecimal("15"), 2);
//        service.addProduct(testMouse2);
//        service.addProduct(testKeyboard2);
//        System.out.println("Исходное состояние: " + "Mouse: "
//                + testMouse2.getQuantity() + ", " + "Keyboard: " + testKeyboard2.getQuantity());
//        try {
//            service.processOrder(testMouse2.getId(), 3, testKeyboard2.getId(), 5);
//        } catch (IllegalArgumentException e) {
//            System.out.println("Expected error: " + e.getMessage());
//        }
//
//        System.out.println("Ожидаемый результат: " + "Mouse: "
//                + testMouse2.getQuantity() + ", " + "Keyboard: " + testKeyboard2.getQuantity());
//
    }
}
