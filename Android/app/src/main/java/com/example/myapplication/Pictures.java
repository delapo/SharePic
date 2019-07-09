package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Pictures {
    private int nbLikes;
    private int nbComments;
    private String username;
    private String description;
    private String image;
    private Integer id;
    private Integer user_id;
    private String localisation;
    private String date;
    private String created_at;
    private String updated_at;
    private Integer like;
    private Integer comments;


    public Pictures()
    {

    }

    public int getNbLikes() {
        return nbLikes; }

        public void setNbLikes(int nbLikes) { this.nbLikes = nbLikes; }

    public int getNbComments() { return nbComments; }
    public void setNbComments(int nbComments) { this.nbComments = nbComments; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }
}
