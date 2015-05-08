package me.smartco.akstore.user.model;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.common.model.Views;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by libin on 14-11-7.
 */
@Document
public class User extends AbstractDocument {

    @Indexed(unique = true)
    private String username;
    private String password;

    private Boolean active=true;

    protected String token;
    protected Date tokenExpired;

    protected String unionId;

    @DBRef
    private Set<Role> roles=new HashSet<Role>();

    @PersistenceConstructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.active = true;
    }

    public String generateNewToken(){
        token= UUID.randomUUID().toString();
        tokenExpired=new Date(System.currentTimeMillis()+3600000*24*14);
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> roles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @JsonView(Views.Detail.class)
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


    public Boolean hasRole(String name){
        for(Role role: roles()){
            if(role.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public String unionid(){
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String token() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date tokenExpiredAt() {
        return tokenExpired;
    }

    public void setTokenExpired(Date tokenExpired) {
        this.tokenExpired = tokenExpired;
    }
}
