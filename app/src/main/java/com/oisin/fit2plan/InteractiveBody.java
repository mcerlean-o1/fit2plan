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

    Button chestBtn, coreBtn, quadBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.human_body);

        chestBtn = findViewById(R.id.button_chest);
        coreBtn = findViewById(R.id.button_core);
        quadBtn = findViewById(R.id.button_quads);
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

    public void OnCoreClick(View v) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.core_workouts);

        RecyclerView recyclerView = dialog.findViewById(R.id.core_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayCoreWorkouts(recyclerView);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void OnQuadClick(View v) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.quad_workouts);

        RecyclerView recyclerView = dialog.findViewById(R.id.quad_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayQuadWorkouts(recyclerView);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    public void OnShoulderClick(View v) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.shoulder_workouts);

        RecyclerView recyclerView = dialog.findViewById(R.id.shoulder_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayShoulderWorkouts(recyclerView);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    public void OnBicepClick(View v) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.bicep_workouts);

        RecyclerView recyclerView = dialog.findViewById(R.id.bicep_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayBicepWorkouts(recyclerView);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

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

    public void displayCoreWorkouts(RecyclerView recyclerView) {
        List<WorkoutBody> workoutBodyList = new ArrayList<>();

        String sit_up = getResources().getString(R.string.sit_up);
        String burpee = getResources().getString(R.string.burpee);
        String plank = getResources().getString(R.string.plank);
        String crunches = getResources().getString(R.string.crunches);
        String hollow_body_hold = getResources().getString(R.string.hollow_body_hold);

        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Sit Ups")
                .setWorkoutDesc(sit_up)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Burpee")
                .setWorkoutDesc(burpee)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Plank")
                .setWorkoutDesc(plank)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Crunches")
                .setWorkoutDesc(crunches)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Hollow Body Hold")
                .setWorkoutDesc(hollow_body_hold)
                .build());

        recyclerView.setAdapter(new WorkoutBodyAdapter(workoutBodyList));
    }

    public void displayQuadWorkouts(RecyclerView recyclerView) {
        List<WorkoutBody> workoutBodyList = new ArrayList<>();

        String barbell_squats = getResources().getString(R.string.barbell_squats);
        String lunges = getResources().getString(R.string.lunges);
        String step_ups = getResources().getString(R.string.step_ups);
        String pistol_squats = getResources().getString(R.string.pistol_squats);
        String walking_lunges = getResources().getString(R.string.walking_lunges);

        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Barbell Squats")
                .setWorkoutDesc(barbell_squats)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Lunges")
                .setWorkoutDesc(lunges)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Step Ups")
                .setWorkoutDesc(step_ups)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Pistol Squats")
                .setWorkoutDesc(pistol_squats)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Walking Lunges")
                .setWorkoutDesc(walking_lunges)
                .build());

        recyclerView.setAdapter(new WorkoutBodyAdapter(workoutBodyList));
    }

    public void displayShoulderWorkouts(RecyclerView recyclerView) {
        List<WorkoutBody> workoutBodyList = new ArrayList<>();

        String dumbbell_lateral_raises = getResources().getString(R.string.dumbbell_lateral_raises);
        String dumbbell_shoulder_press = getResources().getString(R.string.dumbbell_shoulder_press);
        String dumbell_front_raises = getResources().getString(R.string.dumbbell_front_raises);
        String dumbbell_rear_delt_flies = getResources().getString(R.string.dumbbell_rear_delt_flyes);
        String arnold_press = getResources().getString(R.string.arnold_press);

        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("DB Lat. Raises")
                .setWorkoutDesc(dumbbell_lateral_raises)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("DB Shoulder Presses")
                .setWorkoutDesc(dumbbell_shoulder_press)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("DB Front Raises")
                .setWorkoutDesc(dumbell_front_raises)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("DB Rear Delt Flies")
                .setWorkoutDesc(dumbbell_rear_delt_flies)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Arnold Press")
                .setWorkoutDesc(arnold_press)
                .build());

        recyclerView.setAdapter(new WorkoutBodyAdapter(workoutBodyList));
    }

    public void displayBicepWorkouts(RecyclerView recyclerView) {
        List<WorkoutBody> workoutBodyList = new ArrayList<>();

        String db_bicep_curl = getResources().getString(R.string.dumbbell_bicep_curls);
        String hammer_curl = getResources().getString(R.string.hammer_curl);
        String alt_db_curl = getResources().getString(R.string.alt_db_curl);
        String preacher_curl = getResources().getString(R.string.preacher_curl);
        String incline_db_curl = getResources().getString(R.string.incline_db_curl);

        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("DB Bicep Curl")
                .setWorkoutDesc(db_bicep_curl)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Hammer Curl")
                .setWorkoutDesc(hammer_curl)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Alt. DB Curl")
                .setWorkoutDesc(alt_db_curl)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Preacher Curl")
                .setWorkoutDesc(preacher_curl)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Incline DB Curl")
                .setWorkoutDesc(incline_db_curl)
                .build());

        recyclerView.setAdapter(new WorkoutBodyAdapter(workoutBodyList));
    }
}
