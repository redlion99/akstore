package me.smartco.akstore.common.model;

import java.math.BigDecimal;

/**
 * Created by libin on 15-1-9.
 */
public interface IProductInfo {
    public String getProductId();
    public String getProductName();
    public String getPictureUrl();
    public String getProductRemark();
    public BigDecimal getProductPrice();
}
