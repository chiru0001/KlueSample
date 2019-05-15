package com.volive.klueapp.Models;

import java.io.Serializable;

public class ShippingMethodModel implements Serializable{
    String id;
    String shipping_name;
    String price_sar;
    String shipping_price_usd;
    String shipping_price_aed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShipping_name() {
        return shipping_name;
    }

    public void setShipping_name(String shipping_name) {
        this.shipping_name = shipping_name;
    }

    public String getPrice_sar() {
        return price_sar;
    }

    public void setPrice_sar(String price_sar) {
        this.price_sar = price_sar;
    }

    public String getShipping_price_usd() {
        return shipping_price_usd;
    }

    public void setShipping_price_usd(String shipping_price_usd) {
        this.shipping_price_usd = shipping_price_usd;
    }

    public String getShipping_price_aed() {
        return shipping_price_aed;
    }

    public void setShipping_price_aed(String shipping_price_aed) {
        this.shipping_price_aed = shipping_price_aed;
    }

    public String getShipping_price_kwd() {
        return shipping_price_kwd;
    }

    public void setShipping_price_kwd(String shipping_price_kwd) {
        this.shipping_price_kwd = shipping_price_kwd;
    }

    String shipping_price_kwd;

}
