package me.smartco.akstore.common.model.region;

import me.smartco.akstore.common.model.AbstractDocument;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by libin on 14-12-26.
 */
@Document
public class Region  extends AbstractDocument{
    private String name;
    private Set<City> cities=new HashSet<City>();

    private boolean active=true;

    public Region(String name, Set<City> cities) {
        this.name = name;
        this.cities = cities;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<City> getCities() {
        return cities;
    }

    public void setCities(Set<City> cities) {
        this.cities = cities;
    }
}
