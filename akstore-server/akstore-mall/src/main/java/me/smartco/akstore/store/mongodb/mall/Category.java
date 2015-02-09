package me.smartco.akstore.store.mongodb.mall;

/**
 * Created by libin on 14-11-7.
 */
public class Category {
    private String name;

    public static Category get(String name){
        if(null!=name&&!"".equals(name)){
            return new Category(name);
        }
        return null;
    }



    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
