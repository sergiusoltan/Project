package main.java.util;

/**
 * User: Sergiu Soltan
 */
public enum ProductProperties {
    PRODUCT_ID("productId"),
    PRODUCT_NAME("productName"),
    PRODUCT_DESCRIPTION("description"),
    PRODUCT_BLOB("productBlobKey"),
    PRODUCT_IMAGE("productImageUrl");

    private String id;

    private ProductProperties(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
