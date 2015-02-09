package me.smartco.akstore.store.mongodb.market;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.Views;
import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.common.model.Attachment;
import me.smartco.akstore.store.mongodb.mall.Category;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Created by libin on 14-12-21.
 */
@Document
public class DispatchProduct extends AbstractDocument {

    private String name, shortDescription, description;
    private BigDecimal unitPrice=BigDecimal.ZERO; //每公斤价格
    private float unitWeight=1.0f;
    private Integer stock=0;
    private Integer sold=0;
    private String sn;
    private String origin;
    private String atomName="个";
    private Float atomWeight=1.0f;
    private boolean active=true;
    @DBRef
    private Attachment picture;

    private Category cat0;
    private Category cat1;
    private Category cat2;

    public DispatchProduct(String name, Integer stock, BigDecimal unitPrice) {
        this.name = name;
        this.stock = stock;
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(float unitWeight) {
        this.unitWeight = unitWeight;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public Attachment picture() {
        return picture;
    }

    @JsonView(Views.Brief.class)
    public String getPictureUrl(){
        if(null!=picture)
            return picture.getPath();
        return null;
    }

    public String getPicture_id(){
        if(null!=picture)
            return picture.getId();
        return null;
    }

    public String getPicture_path(){
        if(null!=picture)
            return picture.getPath();
        return null;
    }

    public void setPicture(Attachment picture) {
        this.picture = picture;
    }


    public Category getCat0() {
        return cat0;
    }

    public void setCat0(Category cat0) {
        this.cat0 = cat0;
    }

    public Category getCat1() {
        return cat1;
    }

    public void setCat1(Category cat1) {
        this.cat1 = cat1;
    }

    public Category getCat2() {
        return cat2;
    }

    public void setCat2(Category cat2) {
        this.cat2 = cat2;
    }

    public void setCategories(Category cat0,Category cat1,Category cat2){
        this.cat0=cat0;
        this.cat1=cat1;
        this.cat2=cat2;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAtomName() {
        return atomName;
    }

    public void setAtomName(String atomName) {
        this.atomName = atomName;
    }

    public Float getAtomWeight() {
        return atomWeight;
    }

    public void setAtomWeight(Float atomWeight) {
        this.atomWeight = atomWeight;
    }

    //每个价格
    public BigDecimal getAtomPrice(){
        return BigDecimal.valueOf(atomWeight/unitWeight).multiply(unitPrice).setScale(2,BigDecimal.ROUND_UP);
    }
}
