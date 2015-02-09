package me.smartco.akstore.store.mongodb.market;

import me.smartco.akstore.common.model.Nameable;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;

/**
 * Created by libin on 14-12-21.
 */
public class DispatchOrderItem implements Nameable {
    @DBRef
    private DispatchProduct product;
    private int amount;

    public DispatchOrderItem(DispatchProduct product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public DispatchProduct getProduct() {
        return product;
    }

    public void setProduct(DispatchProduct product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public float getWeight(){
        return amount*product.getAtomWeight();
    }

    public BigDecimal getUnitPrice(){
        return product.getUnitPrice();
    }

    public BigDecimal getTotal(){
        return product.getUnitPrice().multiply(BigDecimal.valueOf(getWeight())).setScale(2,BigDecimal.ROUND_UP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DispatchOrderItem)) return false;

        DispatchOrderItem that = (DispatchOrderItem) o;

        if (product != null ? !product.equals(that.product) : that.product != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return product != null ? product.hashCode() : 0;
    }

    @Override
    public String getName() {
        return product.getName();
    }

}
