package me.smartco.akstore.store.mongodb.mall;

import java.math.BigDecimal;

/**
 * Created by libin on 14-12-9.
 */
public interface IProductShort {

    public String getName();
    public BigDecimal getPrice();
    public String getShortDescription();
    public Integer getSold();
    public String getPictureUrl();
    public String getShopName();
    public Category getCat1();
    public Boolean getActive();
}
