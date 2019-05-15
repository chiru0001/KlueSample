package com.volive.klueapp.Models;

public class CartItemModel {

    String prod_id;
    String brand_name;
    String quantity;
    String pname;
    String prod_image;
    String regular_price_sar;
    String min_quantity;
    String price_sar;
    Boolean isChecked;

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getPrice_sar() {
        return price_sar;
    }

    public void setPrice_sar(String price_sar) {
        this.price_sar = price_sar;
    }


    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getProd_image() {
        return prod_image;
    }

    public void setProd_image(String prod_image) {
        this.prod_image = prod_image;
    }

    public String getRegular_price_sar() {
        return regular_price_sar;
    }

    public void setRegular_price_sar(String regular_price_sar) {
        this.regular_price_sar = regular_price_sar;
    }

    public String getMin_quantity() {
        return min_quantity;
    }

    public void setMin_quantity(String min_quantity) {
        this.min_quantity = min_quantity;
    }
}
