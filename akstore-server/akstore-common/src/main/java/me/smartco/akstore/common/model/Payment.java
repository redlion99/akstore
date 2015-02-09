package me.smartco.akstore.common.model;

import me.smartco.akstore.common.model.PaymentType;

/**
 * Created by libin on 14-11-12.
 */
public class Payment {
    public PaymentType paymentType = PaymentType.cash;
    private String accountId;
    private String accountName;
    private String institute;
    private String note;

    public Payment() {
    }

    public Payment(String paymentType, String accountId) {
        this.paymentType = PaymentType.valueOf(paymentType);
        this.accountId = accountId;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}


