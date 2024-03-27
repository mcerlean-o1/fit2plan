package com.oisin.fit2plan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;

public class FoodDiary extends AppCompatActivity {

    Button button;
    ArrayList<Note> notes;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_diary);
        button = findViewById(R.id.food_add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) FoodDiary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.note_input,null,false);
                EditText edtTitle = viewInput.findViewById(R.id.edt_title);
                EditText edtBreakfast = viewInput.findViewById(R.id.edt_breakfast);
                EditText edtSnack1 = viewInput.findViewById(R.id.edt_snack1);
                EditText edtLunch = viewInput.findViewById(R.id.edt_lunch);
                EditText edtSnack2 = viewInput.findViewById(R.id.edt_snack2);
                EditText edtDinner = viewInput.findViewById(R.id.edt_dinner);



                new AlertDialog.Builder(FoodDiary.this)
                        .setView(viewInput)
                        .setTitle("Add Note")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title =  edtTitle.getText().toString();
                                String breakfast =  edtBreakfast.getText().toString();
                                String snack1 =  edtSnack1.getText().toString();
                                String lunch =  edtLunch.getText().toString();
                                String snack2 =  edtSnack2.getText().toString();
                                String dinner =  edtDinner.getText().toString();

                                Note note = new Note(title,breakfast,snack1,lunch,snack2,dinner);

                                boolean isInserted = new NoteHandler(FoodDiary.this).create(note);
                                
                                if (isInserted){
                                    Toast.makeText(FoodDiary.this, "Entry Saved", Toast.LENGTH_SHORT).show();
                                    loadNotes();
                                } else {
                                    Toast.makeText(FoodDiary.this, "Unable to save entry", Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                            }
                        }).show();

            }
        });
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(FoodDiary.this));

        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new NoteHandler(FoodDiary.this).delete(notes.get(viewHolder.getAdapterPosition()).getId());
                notes.remove(viewHolder.getAdapterPosition());
                noteAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadNotes();
    }

    public ArrayList<Note> readNotes(){
        ArrayList<Note> notes = new NoteHandler(this).readNotes();
        return notes;
    }

    public void loadNotes(){
        notes = readNotes();
        noteAdapter = new NoteAdapter(notes, this, new NoteAdapter.ItemClicked() {
            @Override
            public void onClick(int position, View view) {
                editNote(notes.get(position).getId(),view);
            }
        });
        recyclerView.setAdapter(noteAdapter);

    }

    private void editNote(int noteId, View view){
        NoteHandler noteHandler =new NoteHandler(this);
        Note note = noteHandler.readSingleNote(noteId);
        Intent intent = new Intent(this,EditNote.class);
        intent.putExtra("title", note.getTitle());
        intent.putExtra("breakfast", note.getBreakfast());
        intent.putExtra("snack1", note.getSnack1());
        intent.putExtra("lunch", note.getLunch());
        intent.putExtra("snack2", note.getSnack2());
        intent.putExtra("dinner", note.getDinner());
        intent.putExtra("id", note.getId());

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        startActivityForResult(intent,1,optionsCompat.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            loadNotes();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}

