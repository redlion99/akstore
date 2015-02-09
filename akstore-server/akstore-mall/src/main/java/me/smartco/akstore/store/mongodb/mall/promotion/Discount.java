package me.smartco.akstore.store.mongodb.mall.promotion;

import me.smartco.akstore.common.model.Account;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;

/**
 * Created by libin on 14-11-16.
 */
public class Discount {
    private String name;
    @DBRef
    private PromotionRule promotionRule;
    private BigDecimal amount;

    @PersistenceConstructor
    public Discount(String name, BigDecimal amount, PromotionRule promotionRule) {
        this.name = name;
        this.amount = amount;
        this.promotionRule = promotionRule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account account(){
        return promotionRule.getAccount();
    }
}
