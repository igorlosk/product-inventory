package repository;

import model.Product;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ProductRepository {

    private final Map<Long, Product> products = new HashMap<>();

    public void save(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (products.containsKey(product.getId())) {
            throw new IllegalArgumentException("Product with id=" + product.getId() + " already exists");
        }
        products.put(product.getId(), product);
    }

    public Product findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Product ID must be positive and not null");
        }
        return products.get(id);
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public boolean existsById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Product ID must be positive and not null"
            );
        }
        return products.containsKey(id);
    }
}