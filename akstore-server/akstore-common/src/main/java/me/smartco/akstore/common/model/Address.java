package me.smartco.akstore.common.model;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.util.Assert;

/**
 * Created by libin on 14-11-7.
 */
public class Address {
    private final String street, city, province;

    /**
     * Creates a new {@link Address} from the given street, city and province.
     *
     * @param street must not be {@literal null} or empty.
     * @param city must not be {@literal null} or empty.
     * @param province must not be {@literal null} or empty.
     */
    public Address(String street, String city, String province) {

        Assert.hasText(street, "Street must not be null or empty!");
        Assert.hasText(city, "City must not be null or empty!");
        Assert.hasText(province, "Country must not be null or empty!");

        this.street = street;
        this.city = city;
        this.province = province;
    }

    /**
     * Returns a copy of the current {@link Address} instance which is a new entity in terms of persistence.
     *
     * @return
     */
    public Address copy() {
        return new Address(this.street, this.city, this.province);
    }

    /**
     * Returns the street.
     *
     * @return
     */
    @JsonView(Views.Detail.class)
    public String getStreet() {
        return street;
    }

    /**
     * Returns the city.
     *
     * @return
     */
    @JsonView(Views.Detail.class)
    public String getCity() {
        return city;
    }

    /**
     * Returns the province.
     *
     * @return
     */
    @JsonView(Views.Detail.class)
    public String getProvince() {
        return province;
    }


    @Override
    public String toString() {
        return province+city+street;
    }

    public String getString(){
        return toString();
    }

}
