package me.smartco.akstore.transaction.model;

import me.smartco.akstore.common.model.OrderStatus;
import me.smartco.akstore.transaction.util.UUIDUtil;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by libin on 15-1-9.
 */
@Entity
public class OrderHistory implements Serializable {

    @Id
    private String id= UUIDUtil.timeBaseId();
    private Date createTime=new Date();

    @ManyToOne(cascade= CascadeType.ALL)
    private OrderEntity orderEntity;

    public int status= OrderStatus.created;

    public OrderHistory() {
    }

    public OrderHistory(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
