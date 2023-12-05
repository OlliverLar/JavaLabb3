package service;

import entities.Category;
import entities.Product;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseTest {

    @Test
    void addProductCorrectly() {
        Warehouse warehouse = new Warehouse();
        Product product = warehouse.addProduct("TestProduct", Category.PC_SPEL, 5, LocalDate.now());

        assertNotNull(product);
        assertEquals("TestProduct", product.name());
        assertEquals(Category.PC_SPEL, product.category());
        assertEquals(5, product.rating());
    }

    @Test
    void addProductWithoutNameThrowsException() {
        Warehouse warehouse = new Warehouse();

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                warehouse.addProduct("", Category.PC_SPEL, 5, LocalDate.now())
        );

        String expectedMessage = "Product name can't be empty";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void modifyProductCorrectly() {
        Warehouse warehouse = new Warehouse();
        Product addedProduct = warehouse.addProduct("TestProduct", Category.PC_SPEL, 5, LocalDate.now());
        Product modifedProduct = warehouse.modifyProduct(addedProduct.id(), "NewName", Category.XBOX_SPEL, 6);

        assertNotNull(modifedProduct);
        assertEquals("NewName", modifedProduct.name());
        assertEquals(Category.XBOX_SPEL, modifedProduct.category());
        assertEquals(6, modifedProduct.rating());
    }

    @Test
    void modifyNotExistingProductThrowsException() {
        Warehouse warehouse = new Warehouse();
        int NotRealID = 5200;

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                warehouse.modifyProduct(NotRealID, "NewName", Category.XBOX_SPEL, 6)
        );
        String expectedMessage = "Product not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getAllProductsSuccess() {
        Warehouse warehouse = new Warehouse();

        Product product1 = warehouse.addProduct("Product1", Category.PLAYSTATION_SPEL, 9, LocalDate.now());
        Product product2 = warehouse.addProduct("Product2", Category.XBOX_SPEL, 3, LocalDate.now());
        Product product3 = warehouse.addProduct("Product3", Category.PC_SPEL, 10, LocalDate.now());

        List<Product> allProducts = warehouse.getAllProducts();

        assertEquals(3, allProducts.size());
        assertTrue(allProducts.contains(product1));
        assertTrue(allProducts.contains(product2));
        assertTrue(allProducts.contains(product3));
    }

    @Test
    void getAllProductsNoProductsAdded() {
        Warehouse warehouse = new Warehouse();

        List<Product> allProducts = warehouse.getAllProducts();

        assertTrue(allProducts.isEmpty(), "List should be empty");
    }

    @Test
    void getProductByExistingId() {
        Warehouse warehouse = new Warehouse();
        Product addedProduct = warehouse.addProduct("TestProduct", Category.PC_SPEL, 4, LocalDate.now());
        Product foundProduct = warehouse.getProductById(addedProduct.id());

        assertNotNull(foundProduct);
        assertEquals(addedProduct.id(), foundProduct.id());
    }

    @Test
    void getProductByNonExistingId() {
        Warehouse warehouse = new Warehouse();
        Product foundProduct = warehouse.getProductById(3929);

        assertNull(foundProduct);
    }

    @Test
    void getProductsByCategorySuccessfully() {
        Warehouse warehouse = new Warehouse();

        Product product1 = warehouse.addProduct("Product1", Category.PC_SPEL, 3, LocalDate.now());
        Product product2 = warehouse.addProduct("Product2", Category.PC_SPEL, 7, LocalDate.now());
        Product product3 = warehouse.addProduct("Product3", Category.XBOX_SPEL, 5, LocalDate.now());

        List<Product> pcProducts = warehouse.getProductsByCategory(Category.PC_SPEL);

        assertEquals(2, pcProducts.size());
        assertTrue(pcProducts.contains(product1));
        assertTrue(pcProducts.contains(product2));
        assertFalse(pcProducts.contains(product3));
    }

    @Test
    void getProductsByCategoryNoProducts() {
        Warehouse warehouse = new Warehouse();

        List<Product> products = warehouse.getAllProducts();

        assertTrue(products.isEmpty(), "List should be empty");
    }

    @Test
    void getProductsCreatedAfterSuccessful() {
        Warehouse warehouse = new Warehouse();
        LocalDate thresholdDate = LocalDate.of(2023, 1, 1);

        Product product1 = warehouse.addProduct("Product1", Category.PC_SPEL, 2, LocalDate.of(2022, 2, 12));
        Product product3 = warehouse.addProduct("Product3", Category.PLAYSTATION_SPEL, 4, LocalDate.of(2023, 11, 30));

        List<Product> productsAfterDate = warehouse.getProductsCreatedAfter(thresholdDate);

        assertEquals(1, productsAfterDate.size());
        assertFalse(productsAfterDate.contains(product1));
        assertTrue(productsAfterDate.contains(product3));
    }

    @Test
    void getProductsCreatedAfterNoProductsCreatedAfterDate() {
        Warehouse warehouse = new Warehouse();
        LocalDate thresholdDate = LocalDate.of(2023, 1, 1);

        Product product1 = warehouse.addProduct("Product1", Category.PLAYSTATION_SPEL, 5, LocalDate.of(2022, 2, 1));

        List<Product> productsAfterDate = warehouse.getProductsCreatedAfter(thresholdDate);

        assertEquals(0, productsAfterDate.size());
        assertFalse(productsAfterDate.contains(product1));
    }

    @Test
    void getModifiedProductsSuccessful() {
        Warehouse warehouse = new Warehouse();
        LocalDate initialDate = LocalDate.of(2023, 1, 1);

        Product product1 = warehouse.addProduct("Product1", Category.PC_SPEL, 2, initialDate);
        Product product2 = warehouse.addProduct("Product2", Category.XBOX_SPEL, 6, initialDate);


        Product modidiedProduct1 = warehouse.modifyProduct(product1.id(), "Product1 Modified", Category.PC_SPEL, 5);


        List<Product> modifiedProducts = warehouse.getModifiedProducts();

        assertEquals(1, modifiedProducts.size());
        assertTrue(modifiedProducts.stream().anyMatch(p -> p.id() == modidiedProduct1.id()));
    }

    @Test
    void getModifiedProductsNoModifiedProducts() {
        Warehouse warehouse = new Warehouse();
        LocalDate initialDate = LocalDate.of(2023, 1, 1);

        Product product1 = warehouse.addProduct("Product1", Category.XBOX_SPEL, 2, initialDate);

        List<Product> modifiedProducts = warehouse.getModifiedProducts();

        assertTrue(modifiedProducts.isEmpty(), "List should be empty");
    }

}