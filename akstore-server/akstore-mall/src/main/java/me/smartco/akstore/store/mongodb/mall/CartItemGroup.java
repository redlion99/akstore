package me.smartco.akstore.store.mongodb.mall;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.Views;
import me.smartco.akstore.store.mongodb.partner.Shop;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by libin on 14-12-24.
 */
public class CartItemGroup {

    @DBRef
    private Shop shop;
    private Map<String,CartItem> cartItemMap = new HashMap<String, CartItem>();

    @PersistenceConstructor
    public CartItemGroup(Shop shop) {
        this.shop = shop;
    }


    public CartItemGroup(Product product, int amount,Referee referee) {
        this.shop=product.shop();
        setItemAmount(product, amount,referee);
    }

    public void addItem(Product product,int amount,Referee referee){
        if(product.getShopId().equals(getShopId())){
            CartItem cartItem=cartItemMap.get(product.getId());
            if(null==cartItem){
                cartItem=new CartItem(product,amount);
                cartItem.setReferee(referee);
                cartItemMap.put(product.getId(),cartItem);
            }else {
                cartItem.addAmount(amount);
            }
        }

    }

    public void setItemAmount(Product product,int amount,Referee referee){
        if(product.getShopId().equals(getShopId())){
            CartItem cartItem=cartItemMap.get(product.getId());
            if(null!=cartItem&&amount==0){
                cartItemMap.remove(cartItem);
            }
            if(null==cartItem){
                cartItem=new CartItem(product,amount);
                cartItem.setReferee(referee);
                cartItemMap.put(product.getId(),cartItem);
            }else{
                cartItem.setAmount(amount);
            }
        }
    }

    @JsonView(Views.Brief.class)
    public Collection<CartItem> getCartItems(){
        return cartItemMap.values();
    }

    public String getShopId(){
        return shop.getId();
    }
    public String getShopName(){
        return shop.getName();
    }
    public double getMaxServeDistance() {
        return shop.getMaxServeDistance();
    }


    public BigDecimal getTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartItemMap.values()) {
            total = total.add(item.getTotal());
        }

        return total;
    }

    public Shop shop(){
        return shop;
    }
}
