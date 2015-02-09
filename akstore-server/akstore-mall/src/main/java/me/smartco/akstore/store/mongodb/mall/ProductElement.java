package me.smartco.akstore.store.mongodb.mall;

import me.smartco.akstore.store.mongodb.market.DispatchProduct;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;

/**
 * Created by libin on 14-12-21.
 */
public class ProductElement {

    @DBRef
    private DispatchProduct product;
    private int quantity;



    public ProductElement(DispatchProduct product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public DispatchProduct getProduct() {
        return product;
    }

    public BigDecimal getPrice(){
        if(null!=product){
            return product.getAtomPrice().multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }

    public void setProduct(DispatchProduct product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductElement)) return false;

        ProductElement that = (ProductElement) o;

        if (product != null ? !product.equals(that.product) : that.product != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return product != null ? product.hashCode() : 0;
    }
}
