package com.oisin.fit2plan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class NoteHandler extends DatabaseHelper {
    public NoteHandler(Context context) {
        super(context);
    }

    public boolean create(Note note) {

        ContentValues values = new ContentValues();

        values.put("title",note.getTitle());
        values.put("breakfast",note.getBreakfast());
        values.put("snack1",note.getSnack1());
        values.put("lunch",note.getLunch());
        values.put("snack2",note.getSnack2());
        values.put("dinner",note.getDinner());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean isSuccessful = db.insert("Food",null,values) >0;
        db.close();
        return isSuccessful;

    }

    public ArrayList<Note> readNotes() {
        ArrayList<Note> notes = new ArrayList<>();

        String sqlQuery = "SELECT * FROM Food ORDER BY id ASC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String breakfast = cursor.getString(cursor.getColumnIndex("breakfast"));
                String snack1 = cursor.getString(cursor.getColumnIndex("snack1"));
                String lunch = cursor.getString(cursor.getColumnIndex("lunch"));
                String snack2 = cursor.getString(cursor.getColumnIndex("snack2"));
                String dinner = cursor.getString(cursor.getColumnIndex("dinner"));

                Note note = new Note(title, breakfast, snack1, lunch, snack2, dinner);
                note.setId(id);
                notes.add(note);
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
        }
        return notes;
    }

    public Note readSingleNote(int id){
        Note note = null;
        String sqlQuery = "SELECT * FROM Food WHERE id="+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);

        if(cursor.moveToFirst()){
            int noteId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String breakfast = cursor.getString(cursor.getColumnIndex("breakfast"));
            String snack1 = cursor.getString(cursor.getColumnIndex("snack1"));
            String lunch = cursor.getString(cursor.getColumnIndex("lunch"));
            String snack2 = cursor.getString(cursor.getColumnIndex("snack2"));
            String dinner = cursor.getString(cursor.getColumnIndex("dinner"));

            note = new Note(title, breakfast, snack1,
                    lunch, snack2, dinner);
            note.setId(noteId);
        }

        cursor.close();
        db.close();
        return note;
    }

    public boolean update(Note note) {
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("breakfast", note.getBreakfast());
        values.put("snack1", note.getSnack1());
        values.put("lunch", note.getLunch());
        values.put("snack2", note.getSnack2());
        values.put("dinner", note.getDinner());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean isSuccessful = db.update("Food", values, "id='"+note.getId()+"'", null) > 0;

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
