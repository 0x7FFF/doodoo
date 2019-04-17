package com.smakhorin.doodoo;

public class cards {
    private String NamePoint;

    public String getHowPoint() {
        return HowPoint;
    }

    public void setHowPoint(String howPoint) {
        HowPoint = howPoint;
    }

    private String HowPoint;


    private String MoneyPay;
    private String imageURL;

    public cards(String NamePoint,String HowPoint, String MoneyPay, String imageURL){
        this.NamePoint = NamePoint;
        this.HowPoint = HowPoint;
        this.MoneyPay= MoneyPay;
        this.imageURL = imageURL;
    }

    public String getNamePoint()
    {
        return NamePoint;
    }
    public void setNamePoint(String NamePoint){
        this.NamePoint = NamePoint;
    }

    public String getMoneyPay()
    {
        return MoneyPay;
    }
    public void setMoneyPay(String MoneyPay){
        this.MoneyPay = MoneyPay;
    }
    public String getImageURL()
    {
        return imageURL;
    }
    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }
}
