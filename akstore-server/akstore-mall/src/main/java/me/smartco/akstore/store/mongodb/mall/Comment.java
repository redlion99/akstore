package me.smartco.akstore.store.mongodb.mall;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.Views;
import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.store.mongodb.partner.Shop;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by libin on 14-11-27.
 */
@Document
public class Comment extends AbstractDocument{


    @DBRef
    private Customer customer;


    @DBRef
    private Shop shop;

    @DBRef
    private Product product;

    private String content;

    private Integer rate;

    @PersistenceConstructor
    public Comment(Customer customer, Integer rate, String content) {
        this.customer = customer;
        this.rate = rate;
        this.content = content;
    }


    public Comment(Customer customer, Product product, Integer rate, String content) {
        this.customer = customer;
        this.product=product;
        this.shop=product.shop();
        this.rate = rate;
        this.content = content;
    }

    public Comment(Customer customer, Shop shop, Integer rate, String content) {
        this.customer = customer;
        this.shop=shop;
        this.rate = rate;
        this.content = content;
    }


    @JsonView(Views.Detail.class)
    public String getProductName(){
        if(null==product)
            return null;
        return product.getName();
    }



    @JsonView(Views.Detail.class)
    public String getShopName(){
        return shop.getName();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    @JsonView(Views.Brief.class)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
