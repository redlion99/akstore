package me.smartco.akstore.store.mongodb.core;

import me.smartco.akstore.store.mongodb.mall.Product;
import me.smartco.akstore.store.mongodb.partner.Shop;

/**
 * Created by libin on 15-1-5.
 */
public class GroupResult {

    private Shop shop;
    private Product product;
    private int count;
    private double total;
    private double avg;


    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    private int positive;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
