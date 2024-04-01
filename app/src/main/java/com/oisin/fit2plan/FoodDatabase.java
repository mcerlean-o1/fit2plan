package com.oisin.fit2plan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "FoodDatabase";


    public FoodDatabase(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlQuery = "CREATE TABLE Food ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date DATE," +
                "breakfast TEXT," +
                "snack1 TEXT," +
                "lunch TEXT," +
                "snack2 TEXT," +
                "dinner TEXT)";
        db.execSQL(sqlQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlQuery = "DROP TABLE IF EXISTS Food";
        db.execSQL(sqlQuery);
        onCreate(db);

    }
}