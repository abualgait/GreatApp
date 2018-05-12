package com.abualgait.abual.greatapp;

public class User {
    private String profile;
    private String email;

    public User() {
    }

    public User(String profile, String email) {
        this.profile = profile;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }


}