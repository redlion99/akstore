package me.smartco.akstore.common.model;

import java.util.Date;

/**
 * Created by libin on 14-11-16.
 */
public class Location {

    private Double lat;
    private Double lng;
    private Date updatedAt;

    public Location(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
        updatedAt=new Date();
    }

    public static Location copy(Double[] location) {
        if(null!=location)
            return new Location(location[0],location[1]);
        return null;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Location(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
