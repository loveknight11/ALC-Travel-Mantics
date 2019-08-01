package com.example.travelmantics;

public class TravelDeal {
    private String mId;
    private String mTitle;
    private String mPrice;
    private String mDescription;
    private String mImageUrl;

    public TravelDeal(String title, String price, String description, String imageUrl) {
        mTitle = title;
        mPrice = price;
        mDescription = description;
        mImageUrl = imageUrl;
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
