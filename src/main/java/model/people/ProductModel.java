package main.java.model.people;

import main.java.util.Utils;

/**
 * User: Sergiu Soltan
 */
public class ProductModel {
    private Long productId;
    private String productName;
    private String description;
    private String productBlobKey;
    private String uploadUrl;

    public static ProductModel fromString(String model){
        return Utils.getInstance().fromJson(model, ProductModel.class);
    }

    public ProductModel() {
    }

    public ProductModel(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductBlobKey() {
        return productBlobKey;
    }

    public void setProductBlobKey(String productBlobKey) {
        this.productBlobKey = productBlobKey;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
}
