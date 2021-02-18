package com.schoolforum.www.entity;

import java.io.Serializable;

public class Reply implements Serializable {

    private String id;
    private String comment;
    private String replyman;
    private String replytime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReplyman() {
        return replyman;
    }

    public void setReplyman(String replyman) {
        this.replyman = replyman;
    }

    public String getReplytime() {
        return replytime;
    }

    public void setReplytime(String replytime) {
        this.replytime = replytime;
    }

    public Reply() {
    }

    public Reply(String id, String comment, String replyman, String replytime) {
        this.id = id;
        this.comment = comment;
        this.replyman = replyman;
        this.replytime = replytime;
    }
}
