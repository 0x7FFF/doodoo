package com.smakhorin.doodoo.foodcard;

public class FoodCard {

    private String name;
    private String price;
    private String imageURL;
    private String locationCount;

    public FoodCard(String name, String locationCount, String price, String imageURL){
        this.name = name;
        this.locationCount = locationCount;
        this.price = price;
        this.imageURL = imageURL;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getLocationCount() {
        return locationCount;
    }
    public void setLocationCount(String locationCount) {
        this.locationCount = locationCount;
    }
    public String getPrice()
    {
        return price;
    }
    public void setPrice(String Price){
        this.price = Price;
    }
    public String getImageURL()
    {
        return imageURL;
    }
    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }
}
