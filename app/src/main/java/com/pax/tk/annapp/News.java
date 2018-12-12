package com.pax.tk.annapp;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by Petrus on 28.03.2018.
 */

public class News implements Serializable{
    private String title;
    private String link;
    private String description;
    private String article;
    private String rawArticle;
    private String imageurl;
    private Drawable image;


    /**
     * creates a news with a title, a link, a description, an article, the rawArticle and the url of an image
     *
     * @param title title of the news
     * @param link link
     * @param description description of the news
     * @param article article with the information in it
     * @param rawArticle rawArticle
     * @param imageurl url of an image for the news
     */
    public News(String title, String link , String description, String article, String rawArticle, String imageurl) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.article = article;
        this.rawArticle = rawArticle;
        this.imageurl = imageurl;
    }

    /**
     * gets the link of a news
     *
     * @return link of the news
     */
    public String getLink() {
        return link;
    }

    /**
     * gets the title of a news
     *
     * @return title of the news
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets the title of a news
     *
     * @param title title to be set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets the description of a news
     *
     * @return description of the news
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the description of a news
     *
     * @param description description to be set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * gets the article of a news
     *
     * @return article of the news
     */
    public String getArticle() {
        return article;
    }

    /**
     * gets the url of the image of a news
     *
     * @return url of the image of the news
     */
    public String getImageurl() {
        return imageurl;
    }

    /**
     * gets the raw article of a news
     *
     * @return raw article of the news
     */
    public String getRawArticle() {
        return rawArticle;
    }

    /**
     * gets the image of a news
     *
     * @return image of the news
     */
    public Drawable getImage() {
        return image;
    }

    /**
     * sets the image of a news
     *
     * @param image image to be set
     */
    public void setImage(Drawable image) {
        if(image != null)
            this.image = image;
        System.out.println("set Image: " + image);
    }

    /**
     * compares this news with an other object
     *
     * @param o object this news shall be compared with
     * @return true if the object equals this news, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        return link != null ? link.equals(news.link) : news.link == null;
    }

    /**
     * creates a small hash code for this news with only the link in it
     *
     * @return small hash code
     */
    @Override
    public int hashCode() {
        return link != null ? link.hashCode() : 0;
    }

    /**
     * converts a news into a String
     *
     * @return String
     */
    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", article='" + article + '\'' +
                ", imageurl='" + imageurl + '\'' +
                '}';
    }

    /**
     * creates the full hash code for this news
     *
     * @return full hash code
     */
    public int fullHashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (article != null ? article.hashCode() : 0);
        result = 31 * result + (imageurl != null ? imageurl.hashCode() : 0);
        return result;
    }
}
