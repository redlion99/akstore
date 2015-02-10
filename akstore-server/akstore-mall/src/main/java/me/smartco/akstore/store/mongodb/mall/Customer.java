package me.smartco.akstore.store.mongodb.mall;


import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.*;
import me.smartco.akstore.store.mongodb.partner.Shop;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by libin on 14-11-7.
 */
@CompoundIndexes({
        @CompoundIndex(name = "customer_ref_to_user", def = "{'user' : 1}", unique=true)
})
@Document
public class Customer extends AbstractDocument {


    @Indexed(unique = true)
    private String loginname;



    private String realname;


    private Contact contact=new Contact();

    private Shipping shippingAddress;
    private Set<Shipping> addresses = new HashSet<Shipping>();

    @DBRef
    private Set<Shop> favoriteShops=new HashSet<Shop>();

    private Attachment avatar;

    /**
     * Creates a new {@link Customer} from the given firstname and lastname.
     *
     * @param id must not be {@literal null} or empty.
     */
    @PersistenceConstructor
    public Customer(String id,String loginname) {

        Assert.notNull(id);
        this.id=id;
        this.loginname=loginname;
    }


    protected Customer() {

    }


    public String getLoginname() {
        if(loginname.length()>10){
            return "1******"+loginname.substring(loginname.length()-4);
        }else {
            return loginname;
        }

    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }



    /**
     * Adds the given {@link me.smartco.akstore.common.model.Address} to the {@link Customer}.
     *
     * @param address must not be {@literal null}.
     */
    public void addAddress(Shipping address) {

        Assert.notNull(address);
        this.addresses.add(address);
        shippingAddress=address;
    }

    public void addFavoriteShop(Shop shop) {

        Assert.notNull(shop);
        favoriteShops.add(shop);
    }

    public void removeFavoriteShop(Shop shop) {

        Assert.notNull(shop);
        favoriteShops.remove(shop);
    }

    public Contact contact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Shipping shippingAddress() {
        return shippingAddress;
    }

    public Set<Shop> favoriteShops() {
        return favoriteShops;
    }

    public Set<Shipping> shippingSet(){
        return addresses;
    }

    public void setShippingAddress(Shipping shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getAvatar(){
        if(null!=avatar){
            return avatar.getPath();
        }else {
            return null;
        }
    }

    public void setAvatar(Attachment avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "{" + shippingAddress +
                '}';
    }
}
