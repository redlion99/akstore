package me.smartco.akstore.common.model;

/**
 * Created by libin on 14-12-20.
 */
public class Contact {
    private String phone;
    private EmailAddress email;
    private String qq;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public EmailAddress getEmail() {
        return email;
    }

    public void setEmail(EmailAddress email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
