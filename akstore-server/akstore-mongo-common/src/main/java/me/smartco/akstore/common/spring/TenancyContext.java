package me.smartco.akstore.common.spring;

/**
 * Created by libin on 15-2-9.
 */
public class TenancyContext {

    public static ThreadLocal<TenancyContext> tenancyContextThreadLocal=new ThreadLocal<TenancyContext>();

    private String name;
    private String slug;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
