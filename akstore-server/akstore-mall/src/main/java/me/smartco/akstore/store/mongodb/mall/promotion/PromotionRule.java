package me.smartco.akstore.store.mongodb.mall.promotion;

import me.smartco.akstore.common.model.Account;
import me.smartco.akstore.common.model.AbstractDocument;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by libin on 14-11-16.
 */
@Document
public class PromotionRule extends AbstractDocument {
    private String name;
    @DBRef
    private Account account;
    private List<Condition> conditionList=new ArrayList<Condition>();
    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean eval(Object face){
        Fact fact=new Fact(face);
        Iterator<Condition> it=conditionList.iterator();
        try {
            if(it.hasNext()) {
                Condition condition = fact.on(it.next());
                while (it.hasNext()) {
                    condition.concat(it.next());
                }
                return condition.getResult();
            }
        }catch (Exception e){

        }
        return false;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
