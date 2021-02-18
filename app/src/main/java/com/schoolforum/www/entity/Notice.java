package com.schoolforum.www.entity;

import java.io.Serializable;

public class Notice implements Serializable {

    private String id;
    private String title;
    private String content;
    private String createman;
    private String createtime;
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

    public String getCreateman() {
        return createman;
    }

    public void setCreateman(String createman) {
        this.createman = createman;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Notice() {
    }

    public Notice(String id, String title, String content, String createman, String createtime, String picture) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createman = createman;
        this.createtime = createtime;
        this.picture = picture;
    }
}
