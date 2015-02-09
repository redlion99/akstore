package me.smartco.akstore.common.model;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.util.MD5Util;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by libin on 14-11-16.
 */
public class Shipping {

    private String id= UUID.randomUUID().toString();
    public enum ShippingMethod{normal,express,highExpress,selfTake}
    private Address shippingAddress;
    private ShippingMethod shippingMethod;

    private BigDecimal shippingFee=BigDecimal.ZERO;

    private String receiver;

    private String phone;

    private Double[] location;

    public Shipping(Address shippingAddress, String receiver,String phone, ShippingMethod shippingMethod) {
        this.shippingAddress = shippingAddress;
        this.shippingMethod = shippingMethod;
        this.receiver = receiver;
        this.phone=phone;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonView(Views.Detail.class)
    public Location getLocation() {
        return Location.copy(location);
    }
    public void setLocation(Location loc) {
        this.location=new Double[]{loc.getLat(),loc.getLng()};
    }

    @Override
    public String toString() {
        return shippingAddress+" "+receiver + " "+ phone;
    }

    public String getId() {
        if(null==id)
            return MD5Util.MD5(toString());
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
