package com.example.travelmantics;

import java.io.Serializable;

public class TravelDeal implements Serializable {
    private String mId;
    private String mTitle;
    private String mPrice;
    private String mDescription;
    private String mImageUrl;

    public String getImageName() {
        return mImageName;
    }

    public void setImageName(String imageName) {
        mImageName = imageName;
    }

    private String mImageName;

    public TravelDeal(String title, String price, String description, String imageUrl, String imageName) {
        mTitle = title;
        mPrice = price;
        mDescription = description;
        mImageUrl = imageUrl;
        mImageName = imageName;
    }

    public TravelDeal(){}

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
