package me.smartco.akstore.common.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by libin on 15-1-4.
 */
public class ShippingObject {

    private Shipping shippingAddress;
    private Set<Shipping> addresses = new HashSet<Shipping>();

    public ShippingObject(Shipping shippingAddress, Set<Shipping> addresses) {
        this.shippingAddress = shippingAddress;
        this.addresses = addresses;
    }

    public Shipping getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Shipping shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Set<Shipping> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Shipping> addresses) {
        this.addresses = addresses;
    }
}
