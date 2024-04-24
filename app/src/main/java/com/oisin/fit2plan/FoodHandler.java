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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("date", food.getDate());
        values.put("mealType", food.getMealType());
        values.put("mealDescription", food.getDescription());


        long isSuccessful = db.insert("Food",null,values);


        if (isSuccessful == -1) {
            Log.e("FoodHandler", "Insert failed");
        } else {
            Log.i("FoodHandler", "Food inserted with ID: " + isSuccessful);
            food.setId((int) isSuccessful);
        }
        db.close();
        return isSuccessful;

    }



    public Food readSingleFood( int id) {



        SQLiteDatabase db = getWritableDatabase();

        String selection = "id = ?";
        String[] selectionArgs = new String[] { String.valueOf(id) };
        Cursor cursor = db.query("Food",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);


            if (cursor != null && cursor.moveToFirst()) {
                Log.d("FoodHandler", "Food found with ID: " + id);
                return Food.fromCursor(cursor);
            } else {
                if (cursor != null) {
                    cursor.close();
                } else {
                    Log.d("FoodHandler", "No foods found");
                }
                db.close();
            }
        return null;
    }

    public boolean update(Food food) {
        ContentValues values = new ContentValues();
        values.put(Column_Date, food.getDate());
        values.put(Column_Meal_Type, food.getMealType());
        values.put(Column_Meal_Description, food.getDescription());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean isSuccessful = db.update("Food", values, "id = ?", new String[] {String.valueOf(food.getId())}) > 0;

        db.close();
        return isSuccessful;
    }

    public boolean delete(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = new String[] {String.valueOf(id)};
        Log.d("FoodHandler", "Attempting to delete item with ID: " + id);

        Cursor cursor = db.rawQuery("SELECT id FROM Food WHERE id = ?", selectionArgs);
        boolean exists = cursor.moveToFirst();

        if (!exists) {
            Log.e("FoodHandler", "Item doesn't exist, ID: " + id);
            db.close();
            cursor.close();
            return false;
        } else {
            Log.d("FoodHandler", "Item exists:");
        }

        int isDeleted = db.delete("Food", "id = ?", selectionArgs);
        cursor.close();
        db.close();

        if (isDeleted > 0) {
            Log.i("FoodHandler", "Deleted Entry: " + id);
            return true;

        } else {
            Log.e("FoodHandler", "Entry not deleted: " + id);
            return false;
        }


    }

    public ArrayList<Food> readFoodsByDate(String date) {
        ArrayList<Food> foods = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = "date = ?";
        String[] selectionArgs = new String[] { date };
        Cursor cursor = db.query("Food",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(Column_Id));
                String mealType = cursor.getString(cursor.getColumnIndex(Column_Meal_Type));
                String mealDesc = cursor.getString(cursor.getColumnIndex(Column_Meal_Description));
                foods.add(new Food(id, date, mealType, mealDesc));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return foods;


    }

    public boolean addPhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put("mealID", photo.getMealID());
        vals.put("PhotoPath", photo.getPhotoPath());



        long result = db.insert("Photos", null, vals);

        if (result == -1) {
            Log.e("FoodHandler", "Photo Insert failed");
        } else {
            Log.i("FoodHandler", "Photo inserted with ID: " + result);
            photo.setPhotoID((int) result);
        }
        db.close();
        return result != -1;

    }

    public List<Photo> getPhotos(int mealId) {
        List<Photo> photos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Photos", new String[] { "photoID", "mealID", "PhotoPath" },
                "mealID = ?", new String[]{String.valueOf(mealId)},
                null, null, null);
        if (cursor != null) {
            Log.d("FoodHandler", "Number of photos retrieved: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                {
                    do {
                        int photoId = cursor.getInt(cursor.getColumnIndex("photoID"));
                        String photoPath = cursor.getString(cursor.getColumnIndex("PhotoPath"));
                        Photo photo = new Photo(photoId, mealId, photoPath);
                        photos.add(photo);
                        Log.d("FoodHandler", "Retrieved photo: ID=" + photoId + ", Path=" + photoPath);
                    } while (cursor.moveToNext());
                }
            } else {
                Log.e("FoodHandler", "No photos found for mealID: " + mealId);
            }
            cursor.close();
            db.close();
        }

        return photos;
    }

    public boolean deletePhoto(int photoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleteRows = db.delete("Photos", "photoID =?", new String[]{String.valueOf(photoId)});
        db.close();
        return deleteRows > 0;
    }


}
