package com.oisin.fit2plan;

public class Photo {

    private int photoID;
    private int mealID;
    private String photoPath;

    public Photo(int photoID, int mealID, String photoPath) {
        this.photoID = photoID;
        this.mealID = mealID;
        this.photoPath = photoPath;
    }

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }

    public int getMealID() {
        return mealID;
    }

    public void setMealID(int mealID) {
        this.mealID = mealID;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
