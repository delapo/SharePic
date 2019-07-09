package com.example.myapplication;

import android.net.Uri;

public class ProfilPicture {

    private Uri profil_picture;

    public ProfilPicture() {

    }

    public ProfilPicture(Uri profil_picture) {

    this.profil_picture = profil_picture;

    }

    public Uri getProfil_picture() {

        return this.profil_picture;
    }

    public void setProfil_picture(Uri profil_picture) {

        this.profil_picture = profil_picture;
    }
}
