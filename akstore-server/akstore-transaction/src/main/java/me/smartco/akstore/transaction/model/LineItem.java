package me.smartco.akstore.transaction.model;

import me.smartco.akstore.common.model.IProductInfo;
import me.smartco.akstore.common.model.Nameable;
import me.smartco.akstore.transaction.util.UUIDUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by libin on 15-1-9.
 */
@Entity
public class LineItem  implements IProductInfo,Serializable,Nameable {
    @Id
    private String id=UUIDUtil.timeBaseId();

    @ManyToOne(cascade= CascadeType.ALL)
    private OrderEntity orderEntity;

    private Date createTime=new Date();
    private int amount;

    private String productId;
    private String productName;
    private String pictureUrl;
    private String productRemark;
    private String refereeId;
    private BigDecimal productPrice=BigDecimal.ZERO;

    public LineItem() {
    }

    public LineItem(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
        orderEntity.lineItems().add(this);
    }

    public String getId() {
        return id;
    }

    public BigDecimal getUnitPrice() {
        return getProductPrice();
    }


    public BigDecimal getTotal() {
        return getProductPrice().multiply(BigDecimal.valueOf(amount));
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderEntity orderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

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

    public String getProductRemark() {
        return productRemark;
    }

    public void setProductRemark(String productRemark) {
        this.productRemark = productRemark;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getRefereeId() {
        return refereeId;
    }

    public void setRefereeId(String refereeId) {
        this.refereeId = refereeId;
    }

    @Override
    public String getName() {
        return getProductName();
    }
}
