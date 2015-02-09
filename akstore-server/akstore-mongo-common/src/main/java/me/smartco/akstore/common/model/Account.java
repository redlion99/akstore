package me.smartco.akstore.common.model;

import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.common.model.Payment;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by libin on 14-11-12.
 */
@Document
public class Account extends AbstractDocument {

    public enum AccountType{primary,selfRun,partner,staff,promotion,shippingFee}

    private String name;
    private Payment payment;
    private AccountType type;

    @PersistenceConstructor
    public Account(String name,AccountType type) {
        this.name = name;
        this.type = type;
        payment=new Payment();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Payment payment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
