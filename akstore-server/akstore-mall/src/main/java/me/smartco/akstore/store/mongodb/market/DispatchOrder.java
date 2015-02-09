package me.smartco.akstore.store.mongodb.market;

import me.smartco.akstore.common.model.IShopInfo;
import me.smartco.akstore.common.model.Nameable;
import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.common.model.OrderStatus;
import me.smartco.akstore.store.mongodb.partner.Partner;
import me.smartco.akstore.store.mongodb.partner.Shop;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by libin on 14-12-21.
 */
@Document
public class DispatchOrder extends AbstractDocument implements Nameable,IShopInfo {
    private Shop shop;
    @DBRef
    private Partner partner;


    private Set<DispatchOrderItem> orderItems=new HashSet<DispatchOrderItem>();

    private int status= OrderStatus.created;

    public DispatchOrder(Shop shop) {
        this.shop = shop;
        this.partner=shop.partner();
    }

    public Set<DispatchOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
    public String getStatusText() {
        return OrderStatus.text(this.status);
    }

    public void addItem(DispatchProduct product, int amount){
        orderItems.add(new DispatchOrderItem(product,amount));
    }

    public boolean hasFlag(int status){
        return OrderStatus.hasFlag(this.status, status);
    }

    public BigDecimal getTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (DispatchOrderItem item : orderItems) {
            total = total.add(item.getTotal());
        }

        if(total.doubleValue()<0.0){
            total=BigDecimal.ZERO;
        }

        return total;
    }

    public void merge(Map<DispatchProduct,Integer> elements){
        for(DispatchProduct dp:elements.keySet()){
            boolean dpExists=false;
            for(DispatchOrderItem item:orderItems){
                if(item.getProduct().equals(dp)){
                    item.setAmount(item.getAmount()+elements.get(dp));
                    dpExists=true;
                }
            }
            if(!dpExists){
                orderItems.add(new DispatchOrderItem(dp,elements.get(dp)));
            }
        }
    }

    @Override
    public String getName() {
        return shop.getName();
    }


    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public Date getCreateTime() {
        return super.getCreateTime();
    }

    @Override
    public String getShopName() {
        return shop.getName();
    }

    @Override
    public String getShopId() {
        return shop.getId();
    }

    @Override
    public String getShopPhone() {
        return shop.getContact().getPhone();
    }

    @Override
    public String getShopAddress() {
        return shop.getAddress().toString();
    }
}
