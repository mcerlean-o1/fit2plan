package com.oisin.fit2plan;

public class User {

    private String username;
    private String email;
    private String profilePicture;
    private int age;
    private double weight;
    private double height;
    private String gender;
    private String activityLevel;

    public User() {
    }

    public User (String username, String email, String profilePicture, int age, double weight, double height, String gender, String activityLevel) {
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.gender = gender;
        this.activityLevel = activityLevel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }
}
