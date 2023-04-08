package com.example.scouto.models;

@SuppressWarnings("unused")
public class User {
    String fullName;
    String email;
    String dob;
    String gender;

    public User(String fullName, String email, String gender, String dob) {
        this.fullName = fullName;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
