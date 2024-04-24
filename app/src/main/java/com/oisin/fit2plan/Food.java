package com.oisin.fit2plan;

import android.database.Cursor;

public class Food {

    private int id;
    private String date;
    private String mealType;
    private String description;


    public Food(int id, String date, String mealType, String description) {
        this.id = id;
        this.date = date;
        this.mealType = mealType;
        this.description = description;
    }

    public static Food fromCursor(Cursor cursor) {
        if (cursor.moveToFirst()) {
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String mealType = cursor.getString(cursor.getColumnIndex("mealType"));
            String mealDesc = cursor.getString(cursor.getColumnIndex("mealDescription"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));

            Food food = new Food(id, date, mealType, mealDesc);
            food.setId(id);
            return food;
        }
        return null;
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