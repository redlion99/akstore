package me.smartco.akstore.common.model;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by libin on 14-11-12.
 */
@CompoundIndexes({
        @CompoundIndex(name = "staff_ref_to_account", def = "{'account' : 1}", unique=true)
})
@Document
public class Staff extends AbstractDocument {

    @Indexed(unique = true)
    private String loginname;

    @DBRef
    private Account account;


    private Date updatedAt;

    private Double[] location;


    @PersistenceConstructor
    public Staff(String loginname, String id, Account account) {
        this.id=id;
        this.loginname = loginname;
        this.account = account;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Location getLocation() {
        return Location.copy(location);
    }
    public void setLocation(Location loc) {
        this.location=new Double[]{loc.getLat(),loc.getLng()};
        this.updatedAt=loc.getUpdatedAt();
    }
}
