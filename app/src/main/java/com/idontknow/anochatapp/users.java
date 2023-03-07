package com.idontknow.anochatapp;

public class users {
    String name;
    String uid;
    String email;
    String imageUri;
    String status;


    public users() {
    }

    public users(String name, String uid, String email, String imageUri, String status) {
        this.name = name;
        this.uid = uid;
        this.email = email;
        this.imageUri = imageUri;
        this.status = status;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
