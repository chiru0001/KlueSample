package com.volive.klueapp.Models;

import java.io.Serializable;

public class BrandModel implements Serializable {
    String name;
    String feature_brand;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeature_brand() {
        return feature_brand;
    }

    public void setFeature_brand(String feature_brand) {
        this.feature_brand = feature_brand;
    }

    public String getBrand_logo() {
        return brand_logo;
    }

    public void setBrand_logo(String brand_logo) {
        this.brand_logo = brand_logo;
    }

    String brand_logo;

}
