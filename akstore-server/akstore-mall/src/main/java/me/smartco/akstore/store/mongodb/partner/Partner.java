package me.smartco.akstore.store.mongodb.partner;

import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.common.model.Account;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by libin on 14-11-12.
 */
@CompoundIndexes({
        @CompoundIndex(name = "partner_ref_to_account", def = "{'account' : 1}", unique=true)
})
@Document
public class Partner extends AbstractDocument {

    @DBRef
    private Account account;

    private String name;

    private String description;


    @PersistenceConstructor
    public Partner(Account account, String name) {
        this.account = account;
        this.name = name;
    }

    public Account account() {
        return account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
