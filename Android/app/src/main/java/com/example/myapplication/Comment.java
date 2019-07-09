package com.example.myapplication;

import android.widget.ImageView;

class Comment {

    private String image;
    private String username;
    private String comments;
    private int id;

    public Comment(String username, String comments, int id, String image) {
        this.username = username;
        this.comments = comments;
        this.image = image;
        this.id = id;
    }

    public Comment() {

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
