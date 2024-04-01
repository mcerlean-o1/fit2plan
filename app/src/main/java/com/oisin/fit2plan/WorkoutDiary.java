package com.oisin.fit2plan;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutDiary extends AppCompatActivity {

    Button workout_button;
    ArrayList<Workout> workouts;
    RecyclerView recyclerView;
    WorkoutAdapter workoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_diary);
        workout_button = findViewById(R.id.workout_add_button);
        workout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) WorkoutDiary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.workout_input,null,false);
                EditText edtDate = viewInput.findViewById(R.id.edt_date);
                EditText edtA1 = viewInput.findViewById(R.id.edt_a1);
                EditText edtA2 = viewInput.findViewById(R.id.edt_a2);
                EditText edtB1 = viewInput.findViewById(R.id.edt_b1);
                EditText edtB2 = viewInput.findViewById(R.id.edt_b2);
                EditText edtC1 = viewInput.findViewById(R.id.edt_c1);
                EditText edtC2 = viewInput.findViewById(R.id.edt_c2);
                EditText edtMultiNotes = viewInput.findViewById(R.id.edt_multiNotes);



                new AlertDialog.Builder(WorkoutDiary.this)
                        .setView(viewInput)
                        .setTitle("Add Workout")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String date =  edtDate.getText().toString();
                                String a1 =  edtA1.getText().toString();
                                String a2 =  edtA2.getText().toString();
                                String b1 =  edtB1.getText().toString();
                                String b2 =  edtB2.getText().toString();
                                String c1 =  edtC1.getText().toString();
                                String c2 =  edtC2.getText().toString();
                                String multiNotes =  edtMultiNotes.getText().toString();

                                Workout workout = new Workout(date,a1,a2,b1,b2,c1,c2,multiNotes);

                                boolean isInserted = new WorkoutHandler(WorkoutDiary.this).create(workout);

                                if (isInserted){
                                    Toast.makeText(WorkoutDiary.this, "Entry Saved", Toast.LENGTH_SHORT).show();
                                    loadWorkouts();
                                } else {
                                    Toast.makeText(WorkoutDiary.this, "Unable to save entry", Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                            }
                        }).show();

            }
        });
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(WorkoutDiary.this));

        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            //When swiping holder, we want to call the handler to delete it.
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new WorkoutHandler(WorkoutDiary.this).delete(workouts.get(viewHolder.getAdapterPosition()).getId());
                workouts.remove(viewHolder.getAdapterPosition());
                workoutAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadWorkouts();
    }

    public ArrayList<Workout> readWorkouts(){
        ArrayList<Workout> workouts = new WorkoutHandler(this).readWorkouts();
        return workouts;
    }

    public void loadWorkouts(){
        workouts = readWorkouts();
        workoutAdapter = new WorkoutAdapter(workouts, this, new WorkoutAdapter.ItemClicked() {
            @Override
            public void onClick(int position, View view) {
                editWorkout(workouts.get(position).getId(),view);
            }
        });
        recyclerView.setAdapter(workoutAdapter);

    }

    private void editWorkout(int workoutId, View view){
        WorkoutHandler workoutHandler =new WorkoutHandler(this);
        Workout workout = workoutHandler.readSingleWorkout(workoutId);
        Intent intent = new Intent(this,EditWorkout.class);
        intent.putExtra("date", workout.getDates());
        intent.putExtra("a1", workout.getA1());
        intent.putExtra("a2", workout.getA2());
        intent.putExtra("b1", workout.getB1());
        intent.putExtra("c1", workout.getC1());
        intent.putExtra("c2", workout.getC2());
        intent.putExtra("multiNotes", workout.getMultiNotes());
        intent.putExtra("id", workout.getId());

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        startActivityForResult(intent, 1, optionsCompat.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            loadWorkouts();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}

