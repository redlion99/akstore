package me.smartco.akstore.transaction.model;


import me.smartco.akstore.transaction.util.UUIDUtil;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by libin on 15-1-9.
 */
@Entity
public class OrderGroup {
    @Id
    private String id= UUIDUtil.timeBaseId();
    private String customerId;

    public OrderGroup() {
    }

    public OrderGroup(String customerId) {
        this.customerId = customerId;
    }

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
}
