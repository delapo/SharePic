package com.example.myapplication;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    private String password;
    private String password_confirmation;
    private Integer isconnect;
    @SerializedName("profil_picture")
    @Expose
    private String profil_picture;
    @SerializedName("id")
    @Expose
    private Integer id;
    private String token;



    public User() {

    }

    public User(String name, String lastname, String username, String email, String password, String password_confirmation, Integer isconnect, String profil_picture) {
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.isconnect = isconnect;
        this.profil_picture = profil_picture;
    }


    public Integer getId() {
        return this.id;
    }
    public String getToken() {
        return this.token;
    }

    public void setId( Integer id) {
         this.id = id;
    }
    public void setToken(String token) {

        this.token = token;
    }

    public String getName()
    {
        return this.name;
    }

    public String getUsername() {

        return this.username;
    }

    public String getLastname() {

        return this.lastname;
    }

    public String getEmail() {

        return this.email;
    }

    public String getPassword() {

        return this.password;
    }

    public String getPassword_confirmation() {

        return this.password_confirmation;
    }

    public Integer getIsconnect() {

        return this.isconnect;
    }

    public String getProfil_picture() {

        return this.profil_picture;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLastname(String lastname) {

        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public void setIsconnect(Integer isconnect) {
        this.isconnect = isconnect;
    }

    public void setProfil_picture(String profil_picture) {
        this.profil_picture = profil_picture;
    }
}
