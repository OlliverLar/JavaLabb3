package entities;

import java.time.LocalDate;

public record Product(int id, String name, Category category, int rating, LocalDate createdDate, LocalDate lastModifiedDate) {

    public Product {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name can't be empty");
        }
        if (rating < 0 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 0 and 10");
        }
    }
}