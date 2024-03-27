package com.oisin.fit2plan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class WorkoutHandler extends WorkoutDatabase {
    public WorkoutHandler(Context context) {
        super(context);
    }

    public boolean create(Workout workout) {

        ContentValues values = new ContentValues();

        values.put("dates",workout.getDates());
        values.put("a1",workout.getA1());
        values.put("a2",workout.getA2());
        values.put("b1",workout.getB1());
        values.put("b2",workout.getB2());
        values.put("c1",workout.getC1());
        values.put("c2",workout.getC2());
        values.put("multiNotes",workout.getMultiNotes());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean isSuccessful = db.insert("Workout",null,values) >0;
        db.close();
        return isSuccessful;

    }

    public ArrayList<Workout> readWorkouts() {
        ArrayList<Workout> workouts = new ArrayList<>();

        String sqlQuery = "SELECT * FROM Workout ORDER BY id";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String date = cursor.getString(cursor.getColumnIndex("dates"));
                String a1 = cursor.getString(cursor.getColumnIndex("a1"));
                String a2 = cursor.getString(cursor.getColumnIndex("a2"));
                String b1 = cursor.getString(cursor.getColumnIndex("b1"));
                String b2 = cursor.getString(cursor.getColumnIndex("b2"));
                String c1 = cursor.getString(cursor.getColumnIndex("c1"));
                String c2 = cursor.getString(cursor.getColumnIndex("c2"));
                String multiNotes = cursor.getString(cursor.getColumnIndex("multiNotes"));

                Workout workout = new Workout(date, a1, a2, b1, b2, c1, c2, multiNotes);
                workout.setId(id);
                workouts.add(workout);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
        return workouts;
    }

    public Workout readSingleWorkout(int id){
        Workout workout = null;
        String sqlQuery = "SELECT * FROM Workout WHERE id="+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);

        if(cursor.moveToFirst()){
            int workoutId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String dates = cursor.getString(cursor.getColumnIndex("dates"));
            String a1 = cursor.getString(cursor.getColumnIndex("a1"));
            String a2 = cursor.getString(cursor.getColumnIndex("a2"));
            String b1 = cursor.getString(cursor.getColumnIndex("b1"));
            String b2 = cursor.getString(cursor.getColumnIndex("b2"));
            String c1 = cursor.getString(cursor.getColumnIndex("c1"));
            String c2 = cursor.getString(cursor.getColumnIndex("c2"));
            String multiNotes = cursor.getString(cursor.getColumnIndex("multiNotes"));

            workout = new Workout(dates, a1, a2, b1, b2, c1, c2, multiNotes);
            workout.setId(workoutId);
        }

        cursor.close();
        db.close();
        return workout;
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

        boolean isSuccessful = db.update("Workout", values, "id='"+workout.getId()+"'", null) > 0;

        db.close();
        return isSuccessful;
    }

    public boolean delete(int id) {
        boolean isDeleted;
        SQLiteDatabase db = this.getWritableDatabase();
        isDeleted = db.delete("Workout", "id='" +id+"'",null)>0;
        db.close();
        return isDeleted;
    }


}