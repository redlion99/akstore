package me.smartco.akstore.store.mongodb.mall;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.Views;
import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.common.model.Attachment;
import me.smartco.akstore.store.mongodb.partner.Shop;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.DBRef;

/**
 * Created by libin on 15-1-7.
 */
public class Advertisement extends AbstractDocument {

    @DBRef
    private Attachment picture;

    private Boolean active=true;

    @DBRef
    private Product refProduct;

    @DBRef
    private Shop refShop;

    private String url;

    private int priority=9;

    @JsonView(Views.Brief.class)
    public String getPictureUrl(){
        if(null!=picture)
            return picture.getPath();
        return null;
    }

    public void setPicture(Attachment picture) {
        this.picture = picture;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonView(Views.Brief.class)
    public Product getRefProduct() {
        return refProduct;
    }

    public void setRefProduct(Product refProduct) {
        this.refProduct = refProduct;
    }

    @JsonView(Views.Brief.class)
    public Shop getRefShop() {
        return refShop;
    }

    public void setRefShop(Shop refShop) {
        this.refShop = refShop;
    }

    public static Pageable getDefaultPageable(int page){
        Pageable pageable=new PageRequest(page,5, Sort.Direction.ASC,"priority");
        return pageable;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
