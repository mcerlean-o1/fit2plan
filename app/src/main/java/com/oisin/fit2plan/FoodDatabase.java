package com.oisin.fit2plan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodDatabase extends SQLiteOpenHelper {

    public static final String Column_Id = "id";
    public static final String Column_Date = "date";
    public static final String Column_Meal_Type = "mealType";
    public static final String Column_Meal_Description = "mealDescription";
    private static final int DATABASE_VERSION = 6;

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

        String sqlQuery1 = "CREATE TABLE Photos ( photoID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "mealID INTEGER," +
                "PhotoPath TEXT," +
                "FOREIGN KEY(mealID) REFERENCES Food(id))";
        db.execSQL(sqlQuery1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlQuery = "DROP TABLE IF EXISTS Food";
        String sqlQuery1 = "DROP TABLE IF EXISTS Photos";
        db.execSQL(sqlQuery);
        db.execSQL(sqlQuery1);
        onCreate(db);

    }
}