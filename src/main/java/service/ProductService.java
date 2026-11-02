package service;

import model.Product;
import repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException(
                    "Repository cannot be null"
            );
        }

        this.repository = repository;
    }

    public void addProduct(Product product) {
        repository.save(product);
    }

    public Product getProduct(Long id) {
        Product product = repository.findById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product with id " + id + " does not exist");
        }
        return product;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public void updatePrice(Long id, BigDecimal newPrice) {
        Product product = getProduct(id);
        product.changePrice(newPrice);
    }

    public void restock(Long id, int amount) {
        Product product = getProduct(id);
        product.addQuantity(amount);
    }

    public void reduceStock(Long id, int amount) {
        Product product = getProduct(id);
        product.reduceQuantity(amount);
    }

    public BigDecimal calculateInventoryValue() {
        BigDecimal total = BigDecimal.ZERO;
        List<Product> products = repository.findAll();
        for (Product product : products) {
            int quantity = product.getQuantity();
            BigDecimal price = product.getPrice();
            total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
        }

        return total;
    }

    public void processOrder(
            Long firstProductId,
            int firstAmount,
            Long secondProductId,
            int secondAmount
    ) {
        if (firstProductId == null || secondProductId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        if (firstProductId.equals(secondProductId)) {
            throw new IllegalArgumentException("ProductId are the same");
        }

        if (firstAmount <= 0 || secondAmount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Product firstProduct = repository.findById(firstProductId);
        Product secondProduct = repository.findById(secondProductId);

        if (firstProduct == null || secondProduct == null) {
            throw new IllegalArgumentException("Product does not exist");
        }

        if (firstProduct.getQuantity() < firstAmount || secondProduct.getQuantity() < secondAmount) {
            throw new IllegalArgumentException("Not enough products in stock");
        }

        firstProduct.reduceQuantity(firstAmount);
        secondProduct.reduceQuantity(secondAmount);
    }
}
