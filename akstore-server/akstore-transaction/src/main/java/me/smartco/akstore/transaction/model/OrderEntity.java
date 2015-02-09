package me.smartco.akstore.transaction.model;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.*;
import me.smartco.akstore.transaction.util.UUIDUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by libin on 15-1-9.
 */
@Entity
public class OrderEntity implements IShopInfo,Serializable,Nameable {
    @Id
    private String id=UUIDUtil.timeBaseId();

    private String customerId;
    private String partnerId;
    private String shippingAddress;
    private String shippingId;
    private String shopId;
    private String shopName;
    private String shopPhone;
    private String shopAddress;
    private String receiver;
    private String receiverPhone;
    private Double[] location;
    private Date createTime=new Date();
    private Date updateTime=new Date();

    private PaymentType paymentType=PaymentType.cash;

    private BigDecimal total=BigDecimal.ZERO;

    public int status=OrderStatus.created;



    @ManyToOne(cascade= CascadeType.ALL)
    private OrderGroup orderGroup;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER, mappedBy="orderEntity")
    private Set<LineItem> lineItems=new HashSet<LineItem>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingId() {
        return shippingId;
    }

    public void setShippingId(String shippingId) {
        this.shippingId = shippingId;
    }

    public String getShopId() {
        return shopId;
    }

    @Override
    public String getShopPhone() {
        return shopPhone;
    }

    @Override
    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Double[] getLocation() {
        return location;
    }

    public void setLocation(Double[] location) {
        this.location = location;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getStatusText() {
        return OrderStatus.text(this.status);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public OrderGroup getOrderGroup() {
        return orderGroup;
    }

    public void setOrderGroup(OrderGroup orderGroup) {
        this.orderGroup = orderGroup;
    }

    @JsonView(Views.Brief.class)
    public Set<LineItem> lineItems() {
        return lineItems;
    }

    public void setLineItems(Set<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    @JsonView(Views.Brief.class)
    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public static Pageable getDefaultPageable(int page){
        Pageable pageable=new PageRequest(page,10, Sort.Direction.DESC,"updateTime");
        return pageable;
    }


    @Override
    public String getName() {
        return getShopName();
    }
}
