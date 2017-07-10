package com.example.maxim.chopstics.views;

public class PackModel {

    private String mImageUrl = null;

    private String mName = null;

    private String mColor = null;

    public PackModel() {

    }

    public PackModel(String imageUrl, String name, String color) {
        mImageUrl = imageUrl;
        mName = name;
        mColor = color;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getName() {
        return mName;
    }

    public String getColor() {
        return mColor;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setColor(String color) {
        mColor = color;
    }
}
