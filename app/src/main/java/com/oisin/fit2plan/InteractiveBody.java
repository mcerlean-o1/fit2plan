package com.oisin.fit2plan;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InteractiveBody extends AppCompatActivity {

    Button chestBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.human_body);

        chestBtn = findViewById(R.id.button_chest);
    }

    public void OnChestClick(View v) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.chest_workouts);

        RecyclerView recyclerView = dialog.findViewById(R.id.chest_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayChestWorkouts(recyclerView);


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    //This method displays all chest workouts in an android layout page.
    //It uses an array list to display each value while also using the recycler view.
    public void displayChestWorkouts(RecyclerView recyclerView) {
        List<WorkoutBody> workoutBodyList = new ArrayList<>();

        String bench_press = getResources().getString(R.string.bench_press);
        String chest_fly = getResources().getString(R.string.chest_fly);
        String push_up = getResources().getString(R.string.push_up);
        String dips = getResources().getString(R.string.dips);
        String pullover = getResources().getString(R.string.pullover);

        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Bench Press")
                .setWorkoutDesc(bench_press)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Chest Fly")
                .setWorkoutDesc(chest_fly)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Push Up")
                .setWorkoutDesc(push_up)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Dips")
                .setWorkoutDesc(dips)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Dumbbell Pullover")
                .setWorkoutDesc(pullover)
                .build());


        recyclerView.setAdapter(new WorkoutBodyAdapter(workoutBodyList));

    }
}
