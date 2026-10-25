package model;

import java.math.BigDecimal;

public class Product {

    private final Long id;
    private final String name;
    private BigDecimal price;
    private int quantity;

    public Product(Long id, String name, BigDecimal price, int quantity) {
        if (id == null || name == null || price == null) {
            throw new IllegalArgumentException("Input values can't be null");
        }

        if (id <= 0 || name.isBlank() || price.compareTo(BigDecimal.ZERO) < 0  || quantity < 0) {
            throw new IllegalArgumentException("input values can't be negative");
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public void changePrice(BigDecimal newPrice) {
        if (newPrice == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        price = newPrice;
    }

    public void addQuantity (int addToStock) {
        if (addToStock <= 0) {
            throw new IllegalArgumentException("AddQuantity must be greater than 0");
        }
        quantity = Math.addExact(quantity, addToStock);
    }

    public void reduceQuantity (int reduce) {
        if (reduce <= 0) {
            throw new IllegalArgumentException("Reduce must be positive");
        }

        if (reduce > quantity) {
            throw new IllegalArgumentException("Not enough products in stock");
        }
        quantity = quantity - reduce;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
