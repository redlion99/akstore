package me.smartco.akstore.store.mongodb.mall;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.Views;
import me.smartco.akstore.common.model.Location;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;

/**
* Created by libin on 14-11-16.
*/
public class CartItem {
    @DBRef
    @JsonView(Views.Brief.class)
    private Product product;
    private Referee referee;
    private int amount;

    @PersistenceConstructor
    CartItem(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    @JsonView(Views.Brief.class)
    public Product getProduct() {
        return product;
    }

    public Location getLocation(){
        return product.getLocation();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return product.getSalePrice();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public BigDecimal getTotal() {
        return getPrice().multiply(BigDecimal.valueOf(amount));
    }

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }
}
