package me.smartco.akstore.store.mongodb.partner;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.*;
import me.smartco.akstore.common.model.Account;
import me.smartco.akstore.store.mongodb.mall.Referee;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Created by libin on 14-11-7.
 */
@Document
public class Shop extends AbstractDocument {
    private Address address;
    private String name;
    private Double[] location;

    private Boolean active=true;

    private Integer rate=0;
    private Integer commentCount=0;
    private Integer positiveComments=0;

    private BigDecimal minFare=BigDecimal.valueOf(10.0);

    private Contact contact=new Contact();

    @DBRef
    private Attachment picture;
    @DBRef
    private Partner partner;

    private DispatchOptions dispatchOptions=new DispatchOptions();





    private double maxServeDistance=5;

    //copy from partner
    @DBRef
    private Account account;

    @DBRef
    private Referee referee;

    public Shop(String name, Partner partner) {
        this.name = name;
        this.partner = partner;
        this.account=partner.account();
    }

    public static Pageable getDefaultPageable(int page){
        Pageable pageable=new PageRequest(page,50, new Sort(Sort.Direction.DESC,"rate").and(new Sort(Sort.Direction.ASC,"location")));
        return pageable;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @JsonView(Views.Brief.class)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Location getLocation() {
        return Location.copy(location);
    }
    public void setLocation(Location loc) {
        this.location=new Double[]{loc.getLat(),loc.getLng()};
    }

    public void setLocation(Double lat,Double lng) {
        this.location = new Double[]{lat,lng};
    }

    public Partner partner() {
        return partner;
    }

    @JsonView(Views.Detail.class)
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public Attachment picture() {
        return picture;
    }

    @JsonView(Views.Detail.class)
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Integer getPositiveComments() {
        return positiveComments;
    }

    public void setPositiveComments(Integer positiveComments) {
        this.positiveComments = positiveComments;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public BigDecimal getMinFare() {
        return minFare;
    }

    public void setMinFare(BigDecimal minFare) {
        this.minFare = minFare;
    }

    public void setMinFare(double minFare) {
        this.minFare = BigDecimal.valueOf(minFare);
    }

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

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    @JsonView(Views.Detail.class)
    public DispatchOptions getDispatchOptions() {
        return dispatchOptions;
    }

    public void setDispatchOptions(DispatchOptions dispatchOptions) {
        this.dispatchOptions = dispatchOptions;
    }

    public double getMaxServeDistance() {
        return maxServeDistance;
    }

    public void setMaxServeDistance(double maxServeDistance) {
        this.maxServeDistance = maxServeDistance;
    }

    public void setPicture(Attachment picture) {
        this.picture = picture;
    }


}
