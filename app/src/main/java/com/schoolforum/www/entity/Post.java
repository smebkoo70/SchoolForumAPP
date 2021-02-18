package com.schoolforum.www.entity;

import java.io.Serializable;

public class Post implements Serializable {

    private String id;
    private String title;
    private String content;
    private String postman;
    private String posttime;
    private String picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostman() {
        return postman;
    }

    public void setPostman(String postman) {
        this.postman = postman;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Post() {
    }

    public Post(String id, String title, String content, String postman, String posttime, String picture) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.postman = postman;
        this.posttime = posttime;
        this.picture = picture;
    }
}
