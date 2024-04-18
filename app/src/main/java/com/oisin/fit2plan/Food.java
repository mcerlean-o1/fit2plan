package com.oisin.fit2plan;

import android.database.Cursor;

public class Food {

    private int id;
    private String date;
    private String mealType;
    private String description;
    public Cursor cursor;


    public Food(String date, String mealType, String description) {
        this.date = date;
        this.mealType = mealType;
        this.description = description;
    }

    public Food(Cursor cursor) {
        this.cursor = cursor;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}