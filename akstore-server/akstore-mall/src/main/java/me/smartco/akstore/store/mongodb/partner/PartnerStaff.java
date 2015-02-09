package me.smartco.akstore.store.mongodb.partner;

import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.common.model.Contact;
import me.smartco.akstore.store.mongodb.core.user.User;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by libin on 14-11-12.
 */
@CompoundIndexes({
        @CompoundIndex(name = "partnerStaff_ref_to_user", def = "{'user' : 1}", unique=true)
})
@Document
public class PartnerStaff extends AbstractDocument {

    @Indexed(unique = true)
    private String loginname;

    @DBRef
    private Partner partner;

    private Contact contact=new Contact();

    @PersistenceConstructor
    public PartnerStaff(String loginname, String id, Partner partner) {
        this.id=id;
        this.loginname = loginname;
        this.partner = partner;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public void setMobile(String mobile) {
        contact.setPhone(mobile);
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
