package com.volive.klueapp.Models;

import java.util.ArrayList;

public class Title_model {
    String titles;

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    String titleName;
    ArrayList<GiftItemModel> giftItemModels;

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }


    public ArrayList<GiftItemModel> getGiftItemModels() {
        return giftItemModels;
    }

    public void setGiftItemModels(ArrayList<GiftItemModel> giftItemModels) {
        this.giftItemModels = giftItemModels;
    }
}
