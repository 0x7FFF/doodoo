package com.smakhorin.doodoo;

public class FoodCard {
    private String Name;

    public String getLocationCount() {
        return locationCount;
    }

    public void setLocationCount(String locationCount) {
        this.locationCount = locationCount;
    }

    private String locationCount;


    private String Price;
    private String imageURL;

    public FoodCard(String Name, String locationCount, String Price, String imageURL){
        this.Name = Name;
        this.locationCount = locationCount;
        this.Price = Price;
        this.imageURL = imageURL;
    }

    public String getName()
    {
        return Name;
    }
    public void setName(String Name){
        this.Name = Name;
    }

    public String getPrice()
    {
        return Price;
    }
    public void setPrice(String Price){
        this.Price = Price;
    }
    public String getImageURL()
    {
        return imageURL;
    }
    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }
}
