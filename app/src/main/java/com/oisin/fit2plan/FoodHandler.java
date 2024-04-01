package com.oisin.fit2plan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class FoodHandler extends FoodDatabase {
    public FoodHandler(Context context) {
        super(context);
    }

    public boolean create(Food food) {

        ContentValues values = new ContentValues();

        values.put("date", food.getDate());
        values.put("breakfast", food.getBreakfast());
        values.put("snack1", food.getSnack1());
        values.put("lunch", food.getLunch());
        values.put("snack2", food.getSnack2());
        values.put("dinner", food.getDinner());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean isSuccessful = db.insert("Food",null,values) >0;
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
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String breakfast = cursor.getString(cursor.getColumnIndex("breakfast"));
                String snack1 = cursor.getString(cursor.getColumnIndex("snack1"));
                String lunch = cursor.getString(cursor.getColumnIndex("lunch"));
                String snack2 = cursor.getString(cursor.getColumnIndex("snack2"));
                String dinner = cursor.getString(cursor.getColumnIndex("dinner"));

                Food food = new Food(date, breakfast, snack1, lunch, snack2, dinner);
                food.setId(id);
                foods.add(food);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
        return foods;
    }

    public Food readSingleFood(int id){
        Food food = null;
        String sqlQuery = "SELECT * FROM Food WHERE id="+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);

        if(cursor.moveToFirst()){
            int foodId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String breakfast = cursor.getString(cursor.getColumnIndex("breakfast"));
            String snack1 = cursor.getString(cursor.getColumnIndex("snack1"));
            String lunch = cursor.getString(cursor.getColumnIndex("lunch"));
            String snack2 = cursor.getString(cursor.getColumnIndex("snack2"));
            String dinner = cursor.getString(cursor.getColumnIndex("dinner"));

            food = new Food(date, breakfast, snack1,
                    lunch, snack2, dinner);
            food.setId(foodId);
        }

        cursor.close();
        db.close();
        return food;
    }

    public boolean update(Food food) {
        ContentValues values = new ContentValues();
        values.put("date", food.getDate());
        values.put("breakfast", food.getBreakfast());
        values.put("snack1", food.getSnack1());
        values.put("lunch", food.getLunch());
        values.put("snack2", food.getSnack2());
        values.put("dinner", food.getDinner());

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


}
