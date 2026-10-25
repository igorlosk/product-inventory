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
}
