package me.smartco.akstore.store.mongodb.mall;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.Views;
import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.common.model.Shipping;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by libin on 14-11-15.
 */
@CompoundIndexes({
        @CompoundIndex(name = "cart_ref_to_customer", def = "{'customer' : 1}", unique=true)
})
@Document
public class Cart extends AbstractDocument {

    private Map<String,CartItemGroup> cartItemMap = new HashMap<String, CartItemGroup>();

    @DBRef
    private Customer customer;

    public Cart(Customer customer) {
        this.customer = customer;
    }

    @JsonView(Views.Brief.class)
    public Collection<CartItemGroup> getGroups(){
        return cartItemMap.values();
    }

    public BigDecimal getTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (CartItemGroup item : cartItemMap.values()) {
            total = total.add(item.getTotal());
        }

        return total;
    }

    public void clearItems(){
        cartItemMap.clear();
        calculatePriceAndSave();
    }


    public void addItem(Product product,int amount,Referee referee){
        CartItemGroup group=cartItemMap.get(product.getShopId());
        if(null==group){
            group=new CartItemGroup(product,amount,referee);
            cartItemMap.put(product.getShopId(),group);
        }else {
            group.addItem(product, amount,referee);
        }

        calculatePriceAndSave();
    }

    public void setItemAmount(Product product,int amount,Referee referee){
        CartItemGroup group=cartItemMap.get(product.getShopId());
        if(null==group){
            group=new CartItemGroup(product,amount,referee);
            cartItemMap.put(product.getShopId(),group);
        }else {
            group.setItemAmount(product,amount,referee);
        }

        calculatePriceAndSave();
    }

    public void calculatePriceAndSave(){

    }

    @JsonView(Views.Brief.class)
    public Customer getCustomer() {
        return customer;
    }

    public Set<Shipping> getShippingSet(){
        return customer.shippingSet();
    }

    public Shipping getShippingAddress() {
        return customer.shippingAddress();
    }

}
