package com.oisin.fit2plan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodDatabase extends SQLiteOpenHelper {

    public static final String Column_Id = "id";
    public static final String Column_Date = "date";
    public static final String Column_Meal_Type = "mealType";
    public static final String Column_Meal_Description = "mealDescription";
    private static final int DATABASE_VERSION = 7;

    private static final String DATABASE_NAME = "FoodDatabase";


    public FoodDatabase(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlQuery = "CREATE TABLE Food ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT NOT NULL," +
                "mealType TEXT NOT NULL," +
                "mealDescription TEXT)";
        db.execSQL(sqlQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlQuery = "DROP TABLE IF EXISTS Food";
        db.execSQL(sqlQuery);
        onCreate(db);

    }
}