
package service;

import entities.Product;
import entities.Category;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {
    private final Map<Integer, Product> products = new HashMap<>();
    private int currentId = 1;

    public Product addProduct(String name, Category category, int rating, LocalDate createdDate) {
        Product product = new Product(currentId++, name, category, rating, createdDate, createdDate);
        products.put(product.id(), product);
        return new Product(product.id(), product.name(), product.category(), product.rating(), product.createdDate(), product.lastModifiedDate());
    }

    public Product modifyProduct(int id, String newName, Category newCategory, int newRating) {
        Product existingProduct = products.get(id);
        if (existingProduct == null) {
            throw new IllegalArgumentException("Product not found");
        }
        Product updatedProduct = new Product(id, newName, newCategory, newRating, existingProduct.createdDate(), LocalDate.now());
        products.put(id, updatedProduct);
        return new Product(updatedProduct.id(), updatedProduct.name(), updatedProduct.category(), updatedProduct.rating(), updatedProduct.createdDate(), updatedProduct.lastModifiedDate());
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public Product getProductById(int id) {
        return products.get(id);
    }

    public List<Product> getProductsByCategory(Category category) {
        return products.values().stream()
                .filter(product -> product.category() == category)
                .sorted(Comparator.comparing(Product::name))
                .collect(Collectors.toList());
    }

    public List<Product> getProductsCreatedAfter(LocalDate date) {
        return products.values().stream()
                .filter(product -> product.createdDate().isAfter(date))
                .collect(Collectors.toList());
    }

    public List<Product> getModifiedProducts() {
        return products.values().stream()
                .filter(product -> !product.createdDate().isEqual(product.lastModifiedDate()))
                .collect(Collectors.toList());
    }
}
