package me.smartco.akstore.transaction.model;

import me.smartco.akstore.transaction.util.UUIDUtil;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by libin on 14-11-12.
 */
@Entity
public class BillFlow {

    @Id
    private String id= UUIDUtil.timeBaseId();
    private Date createTime=new Date();
    private String name;

    private String accountId;
    private BigDecimal amount=BigDecimal.ZERO;
    private String note;

    @ManyToOne(cascade= CascadeType.ALL)
    private OrderEntity order;

    private BigDecimal balance=BigDecimal.ZERO;


    @PersistenceConstructor
    public BillFlow(String name, String accountId, BigDecimal amount) {
        this.name = name;
        this.accountId = accountId;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
