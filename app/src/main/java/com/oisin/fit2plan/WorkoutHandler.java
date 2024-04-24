package com.oisin.fit2plan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WorkoutHandler extends WorkoutDatabase {
    public WorkoutHandler(Context context) {
        super(context);
    }

    public long create(Workout workout) {


        ContentValues values = new ContentValues();

        values.put("dates", workout.getDates());
        values.put("a1", workout.getA1());
        values.put("a2", workout.getA2());
        values.put("b1", workout.getB1());
        values.put("b2", workout.getB2());
        values.put("c1", workout.getC1());
        values.put("c2", workout.getC2());
        values.put("multiNotes", workout.getMultiNotes());

        SQLiteDatabase db = this.getWritableDatabase();

        long isSuccessful = db.insert("Workout", null, values);

        workout.setId((int) isSuccessful);
        db.close();
        return isSuccessful;

    }

    public Workout readSingleWorkout(int id){
        SQLiteDatabase db = getWritableDatabase();
        String selection = "id = ?";
        String[] selectionArgs = new String[] { String.valueOf(id) };
        Cursor cursor = db.query("Workout",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);


        if (cursor != null && cursor.moveToFirst()) {
            Log.d("WorkoutHandler", "Workout found with ID: " + id);
            return Workout.fromCursor(cursor);
        } else {
            if (cursor != null) {
                cursor.close();
            } else {
                Log.d("WorkoutHandler", "No workout found");
            }
            db.close();
        }
        return null;
    }

    public boolean update(Workout workout) {
        ContentValues values = new ContentValues();
        values.put("dates", workout.getDates());
        values.put("a1", workout.getA1());
        values.put("a2", workout.getA2());
        values.put("b1", workout.getB1());
        values.put("b2", workout.getB2());
        values.put("c1", workout.getC1());
        values.put("c2", workout.getC2());
        values.put("multiNotes", workout.getMultiNotes());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean isSuccessful = db.update("Workout", values, "id= ?",  new String[] {String.valueOf(workout.getId())}) > 0;

        db.close();
        return isSuccessful;
    }


    public ArrayList<Workout> readWorkoutsByDate(String date) {
        ArrayList<Workout> workouts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = "dates = ?";
        String[] selectionArgs = new String[] { date };
        Cursor cursor = db.query("Workout",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String a1 = cursor.getString(cursor.getColumnIndex("a1"));
                String a2 = cursor.getString(cursor.getColumnIndex("a2"));
                String b1 = cursor.getString(cursor.getColumnIndex("b1"));
                String b2 = cursor.getString(cursor.getColumnIndex("b2"));
                String c1 = cursor.getString(cursor.getColumnIndex("c1"));
                String c2 = cursor.getString(cursor.getColumnIndex("c2"));
                String multiNotes = cursor.getString(cursor.getColumnIndex("multiNotes"));

                workouts.add(new Workout(id, date, a1, a2, b1, b2, c1, c2, multiNotes));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return workouts;


    }

    public boolean delete(int id) {
        boolean isDeleted;
        SQLiteDatabase db = this.getWritableDatabase();
        isDeleted = db.delete("Workout", "id='" +id+"'",null)>0;
        db.close();
        return isDeleted;
    }


}