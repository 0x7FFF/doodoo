package com.smakhorin.doodoo;

public class Review {
    private String rating;
    private String author;
    private String text;

    public Review() {}

    public Review(String author, String rating, String text) {
        this.author = author;
        this.rating = rating;
        this.text = text;
    }
    public String getAuthor() {
        return author;
    }

    public String getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setText(String text) {
        this.text = text;
    }
}
