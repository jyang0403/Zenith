package com.example.zenith;

public class FeedArticle {
    String category;
    String title;
    String country;
    String text;
    String image;
    String link;
    String source;

    public FeedArticle(String category, String title, String country, String text, String image, String link, String source) {
        this.category = category;
        this.title = title;
        this.country = country;
        this.text = text;
        this.image = image;
        this.link = link;
        this.source = source;
    }
}
