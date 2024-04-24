package com.oisin.fit2plan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class WorkoutDatabase extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 3;
    private static String DATABASE_NAME = "WorkoutDatabase";
    public WorkoutDatabase(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE Workout (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dates TEXT," +
                "a1 TEXT," +
                "a2 TEXT," +
                "b1 TEXT," +
                "b2 TEXT," +
                "c1 TEXT," +
                "c2 TEXT," +
                "multiNotes TEXT)";

        db.execSQL(sqlQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sqlQuery = "DROP TABLE IF EXISTS Workout";
        db.execSQL(sqlQuery);
        onCreate(db);
    }
}
