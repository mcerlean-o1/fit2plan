package com.oisin.fit2plan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FoodHandler extends FoodDatabase {


    public FoodHandler(Context context) {
        super(context);
    }
    public long create(Food food) {

        ContentValues values = new ContentValues();

        values.put("date", food.getDate());
        values.put("mealType", food.getMealType());
        values.put("mealDescription", food.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();

        long isSuccessful = db.insert("Food",null,values);
        db.close();
        return isSuccessful;

    }

    public ArrayList<Food> readFoods() {
        ArrayList<Food> foods = new ArrayList<>();




        String sqlQuery = "SELECT * FROM Food ORDER BY id ASC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);



        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FoodDatabase.Column_ID)));
                String date = cursor.getString(cursor.getColumnIndex(FoodDatabase.Column_Date));
                String mealType = cursor.getString(cursor.getColumnIndex(FoodDatabase.Column_Meal_Type));
                String mealDescription = cursor.getString(cursor.getColumnIndex(FoodDatabase.Column_Meal_Description));

                Food food = new Food(date, mealType, mealDescription);
                food.setId(id);
                foods.add(food);
            } while (cursor.moveToNext());

            Log.d("FoodHandler", "Cursor count: " + cursor.getCount());
            String[] columnNames = cursor.getColumnNames();
            for (String name : columnNames) {
                Log.d("FoodHandler", "Column name: " + name);
            }
            cursor.close();

        }
        db.close();
        return foods;
    }



    public Food readSingleFood( int id){
        Food food = null;

        // Using query placeholders to safeguard against SQL injection attacks
        String sqlQuery = "SELECT * FROM Food WHERE id= ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, new String[] {String.valueOf(id)});

        if(cursor.moveToFirst()){
            int foodId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Column_ID)));
            String date = cursor.getString(cursor.getColumnIndex(Column_Date));
            String mealType = cursor.getString(cursor.getColumnIndex(Column_Meal_Type));
            String mealDescription = cursor.getString(cursor.getColumnIndex(Column_Meal_Description));

            food = new Food(date, mealType, mealDescription);
            food.setId(foodId);
        }

        db.close();
        return food;
    }

    public boolean update(Food food) {
        ContentValues values = new ContentValues();
        values.put(Column_Date, food.getDate());
        values.put(Column_Meal_Type, food.getMealType());
        values.put(Column_Meal_Description, food.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean isSuccessful = db.update("Food", values, "id='"+ food.getId()+"'", null) > 0;

        db.close();
        return isSuccessful;
    }

    public boolean delete(int id) {
        boolean isDeleted;
        SQLiteDatabase db = this.getWritableDatabase();
        isDeleted = db.delete("Food", "id='" +id+"'",null)>0;
        db.close();
        return isDeleted;
    }

    public ArrayList<Food> readFoodsByDate(String date) {
        ArrayList<Food> foods = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d("readFoodsByDate", "Fetching foods for date: " + date);

        String[] selectionArgs = new String[] { date };
        Cursor cursor = db.query("Food",
                null,
                "date=?",
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                Food food = new Food(cursor);
                foods.add(food);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d("readFoodsByDate", "Number of foods fetched: " + foods.size());
        return foods;


    }

    public boolean addPhoto(long mealId, String photoPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put("mealID", mealId);
        vals.put("PhotoPath", photoPath);

        long result = db.insert("Photos", null, vals);
        db.close();
        return result != 1;
    }

    public List<String> getPhotos(int mealId) {
        List<String> photos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Photos", new String[]{ "PhotoPath" },
                "mealID =?", new String[]{String.valueOf(mealId)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                photos.add(cursor.getString(cursor.getColumnIndex("PhotoPath")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return photos;

    }

    public boolean deletePhoto(int photoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleteRows = db.delete("Photos", "photoID =?", new String[]{String.valueOf(photoId)});
        db.close();
        return deleteRows > 0;
    }

    public Photo getSinglePhoto(int photoId) {
        Photo photo = null;

        String sqlQuery = "SELECT * FROM Photos WHERE photoID= ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, new String[] {String.valueOf(photoId)});

        if(cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("photoID")));
            int mealId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("mealID")));
            String photoPath = cursor.getString(cursor.getColumnIndex("PhotoPath"));

            photo = new Photo(mealId, photoPath);
            photo.setPhotoID(id);
        }

        db.close();
        return photo;

    }



}
