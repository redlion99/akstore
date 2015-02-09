package me.smartco.akstore.store.mongodb.mall;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.Views;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by libin on 15-1-8.
 */
@Document
public class Advisory {

    @DBRef
    private Customer customer;
    private String content;


    public Advisory(Customer customer, String content) {
        this.customer = customer;
        this.content = content;
    }

    @JsonView(Views.Brief.class)
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @JsonView(Views.Detail.class)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
