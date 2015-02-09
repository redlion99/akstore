package me.smartco.akstore.store.mongodb.mall;

import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.common.model.Account;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by libin on 15-1-13.
 */
@Document
public class Referee extends AbstractDocument {

    @DBRef
    private Account account;
    @DBRef
    private Set<Product> delegateProducts=new LinkedHashSet<Product>();

    @PersistenceConstructor
    public Referee(Account account) {
        this.account = account;
    }

    public Account account() {
        return account;
    }

    public Set<Product> delegateProducts() {
        return delegateProducts;
    }

    public void addDelegateProducts(Product product){
        delegateProducts.add(product);
    }

    public void setDelegateProducts(Set<Product> delegateProducts) {
        this.delegateProducts = delegateProducts;
    }

    @Override
    public String getId() {
        return super.getId();
    }
}
