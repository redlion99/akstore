package me.smartco.akstore.common.model.region;

/**
 * Created by libin on 14-12-26.
 */
public class City {
    private String name;

    private boolean active=true;

    public City(String name) {
        this.name = name;
    }

    public String getId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
