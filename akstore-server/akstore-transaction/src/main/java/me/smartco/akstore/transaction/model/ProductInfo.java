package me.smartco.akstore.transaction.model;

import me.smartco.akstore.common.model.IProductInfo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by libin on 15-1-9.
 */
public class ProductInfo implements IProductInfo,Serializable{
    private String productId;
    private String productName;
    private String pictureUrl;
    private String productRemark;
    private BigDecimal productPrice=BigDecimal.ZERO;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductRemark() {
        return productRemark;
    }

    public void setProductRemark(String productRemark) {
        this.productRemark = productRemark;
    }
}
