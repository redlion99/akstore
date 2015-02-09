package me.smartco.akstore.store.mongodb.mall;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.Views;
import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.common.model.Attachment;
import me.smartco.akstore.common.model.Location;
import me.smartco.akstore.store.mongodb.market.DispatchProduct;
import me.smartco.akstore.store.mongodb.partner.Partner;
import me.smartco.akstore.store.mongodb.partner.Shop;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by libin on 14-11-7.
 */
@Document
public class Product extends AbstractDocument {
    private String name, shortDescription, description;
    private BigDecimal price=BigDecimal.ZERO;
    private float discount=1.0f;
    private String remark="";
    private float weight=0f;
    private int quantity=0;
    private Category cat0;
    private Category cat1;
    private Category cat2;
    private Double[] location;
    private Integer stock=0;
    private Integer sold=0;
    private String sn;
    private Boolean feature=false;
    private Map<String, String> attributes = new HashMap<String, String>();


    @DBRef
    private Shop shop;

    //copy from shop
    @DBRef
    private Partner partner;

    @DBRef
    private Attachment picture;

    private Boolean active=true;

    private Set<ProductElement> elementSet=new HashSet<ProductElement>();


    public static Pageable getDefaultPageable(int page){
        Pageable pageable=new PageRequest(page,50, Sort.Direction.DESC,"sold");
        return pageable;
    }


    /**
     * Creates a new {@link Product} with the given name.
     *
     * @param name must not be {@literal null} or empty.
     * @param price must not be {@literal null} or less than or equal to zero.
     */

    public Product(Shop shop,String name, BigDecimal price) {
        this(shop,name, price, null);
    }

    /**
     * Creates a new {@link Product} from the given name and description.
     *
     * @param name must not be {@literal null} or empty.
     * @param price must not be {@literal null} or less than or equal to zero.
     * @param description
     */
    @PersistenceConstructor
    public Product(Shop shop,String name, BigDecimal price, String description) {

        Assert.hasText(name, "Name must not be null or empty!");
        Assert.isTrue(BigDecimal.ZERO.compareTo(price) < 0, "Price must be greater than zero!");
        Assert.notNull(shop);
        this.shop=shop;
        setLocation(shop.getLocation());
        this.partner=shop.partner();
        this.name = name;
        this.price = price;
        this.description = description;
    }

    /**
     * Sets the attribute with the given name to the given value.
     *
     * @param name must not be {@literal null} or empty.
     * @param value
     */
    public void setAttribute(String name, String value) {

        Assert.hasText(name);

        if (value == null) {
            this.attributes.remove(value);
        } else {
            this.attributes.put(name, value);
        }
    }

    /**
     * Returns the {@link Product}'s name.
     *
     * @return
     */
    @JsonView(Views.Brief.class)
    public String getName() {
        return name;
    }

    /**
     * Returns the {@link Product}'s description.
     *
     * @return
     */
    @JsonView(Views.Detail.class)
    public String getDescription() {
        return description;
    }

    /**
     * Returns all the custom attributes of the {@link Product}.
     *
     * @return
     */
    @JsonView(Views.Detail.class)
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    /**
     * Returns the price of the {@link Product}.
     *
     * @return
     */
    @JsonView(Views.Brief.class)
    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getSalePrice() {
        return price.multiply(BigDecimal.valueOf(discount)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private void setLocation(Double[] location) {
        this.location = location;
    }

    public Shop shop() {
        return shop;
    }

    @JsonView(Views.Detail.class)
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setCategories(Category cat0,Category cat1,Category cat2){
        this.cat0=cat0;
        this.cat1=cat1;
        this.cat2=cat2;
    }

    @JsonView(Views.Detail.class)
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

    @JsonView(Views.Detail.class)
    public Category getCat2() {
        return cat2;
    }

    public void setCat2(Category cat2) {
        this.cat2 = cat2;
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


    public String getShopName() {
        return shop.getName();
    }

    public String getShopId() {
        return shop.getId();
    }

    public BigDecimal getMinFare() {
        return shop.getMinFare();
    }

    public void setPicture(Attachment picture) {
        this.picture = picture;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonView(Views.Detail.class)
    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttribute(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @JsonView(Views.Brief.class)
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @JsonView(Views.Detail.class)
    public Location getLocation() {
        return Location.copy(location);
    }
    public void setLocation(Location loc) {
        this.location=new Double[]{loc.getLat(),loc.getLng()};
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
    @JsonView(Views.Detail.class)
    public Boolean getFeature() {
        return feature;
    }

    public void setFeature(Boolean feature) {
        this.feature = feature;
    }

    @JsonView(Views.Detail.class)
    public Integer getStock() {
        return stock;
    }


    public Integer getSold() {
        return sold;
    }

    public String getOrigin() {
        return attributes.get("origin");
    }

    @JsonView(Views.Detail.class)
    public String getSn() {
        return sn;
    }

    public Set<ProductElement> elementSet() {
        return elementSet;
    }

    public void addElement(DispatchProduct product, int quantity){
        elementSet.add(new ProductElement(product,quantity));
    }

    public void clearElement(){
        elementSet.clear();
    }

    //@JsonView(Views.Protected.class)
    public BigDecimal getPredictCost(){
        BigDecimal cost=BigDecimal.ZERO;
        for(ProductElement pe:elementSet){
            cost=cost.add(pe.getPrice());
        }
        return cost.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
