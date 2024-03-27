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

import com.oisin.fit2plan.R;

import java.util.ArrayList;

public class WorkoutDiary extends AppCompatActivity {

    Button button;
    ArrayList<Workout> workouts;
    RecyclerView recyclerView;
    WorkoutAdapter workoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_diary);
        button = findViewById(R.id.workout_add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) WorkoutDiary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.workout_input, null, false);
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
                                String date = edtDate.getText().toString();
                                String A1 = edtA1.getText().toString();
                                String A2 = edtA2.getText().toString();
                                String B1 = edtB1.getText().toString();
                                String B2 = edtB2.getText().toString();
                                String C1 = edtC1.getText().toString();
                                String C2 = edtC2.getText().toString();
                                String MultiNotes = edtMultiNotes.getText().toString();

                                Workout workout = new Workout(date, A1, A2, B1, B2,C1, C2, MultiNotes);

                                boolean isInserted = new WorkoutHandler(WorkoutDiary.this).create(workout);

                                if (isInserted) {
                                    Toast.makeText(WorkoutDiary.this, "Entry Saved", Toast.LENGTH_SHORT).show();
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
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
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

    public ArrayList<Workout> readWorkouts() {
        ArrayList<Workout> workouts = new WorkoutHandler(this).readWorkouts();
        return workouts;
    }

    public void loadWorkouts() {
        workouts = readWorkouts();
        workoutAdapter = new WorkoutAdapter(workouts,this);
        recyclerView.setAdapter(workoutAdapter);
    }
}