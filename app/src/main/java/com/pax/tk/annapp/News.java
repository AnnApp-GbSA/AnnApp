package com.pax.tk.annapp;

import android.graphics.drawable.Drawable;
import android.os.Parcelable;

import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Petrus on 28.03.2018.
 */

public class News implements Serializable{
    private String title;
    private String link;
    private String discription;
    private String article;
    private String imageurl;
    private transient Drawable image;

    public News(String title, String link , String discription, String article, String imageurl, Drawable image) {
        this.title = title;
        this.link = link;
        this.discription = discription;
        this.article = article;
        this.imageurl = imageurl;
        this.image = image;
    }
    public News(String title, String link , String discription, String imageurl, Drawable image) {
        this(title,link,discription,discription,imageurl,image);
    }

    public News(String title, String link , String discription, String article, String imageurl) {
        this.title = title;
        this.link = link;
        this.discription = discription;
        this.article = article;
        this.imageurl = imageurl;
    }
    public News(String title, String link , String discription, String imageurl) {
        this(title,link,discription,discription,imageurl);
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        return link != null ? link.equals(news.link) : news.link == null;
    }

    @Override
    public int hashCode() {
        return link != null ? link.hashCode() : 0;
    }



    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", discription='" + discription + '\'' +
                ", article='" + article + '\'' +
                ", imageurl='" + imageurl + '\'' +
                '}';
    }

    public int fullHashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (discription != null ? discription.hashCode() : 0);
        result = 31 * result + (article != null ? article.hashCode() : 0);
        result = 31 * result + (imageurl != null ? imageurl.hashCode() : 0);
        return result;
    }
}
