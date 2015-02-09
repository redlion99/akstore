package me.smartco.akstore.transaction.model;

import me.smartco.akstore.transaction.util.UUIDUtil;
import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by libin on 14-11-12.
 */
@Entity
public class Bill {

    @Id
    private String id= UUIDUtil.timeBaseId();
    private Date createTime=new Date();
    private String name;
    private String accountId;
    private Date fromDate;
    private Date toDate;
    private Date createDate=new Date();
    private BigDecimal amount;
    private String note;

    @PersistenceConstructor
    public Bill(String name, String accountId, Date fromDate, Date toDate, BigDecimal amount) {
        this.name = name;
        this.accountId = accountId;
        this.fromDate = fromDate;
        this.toDate = toDate;
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

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getId() {
        return id;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
