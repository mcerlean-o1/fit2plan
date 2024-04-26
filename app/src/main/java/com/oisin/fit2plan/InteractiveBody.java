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

    Button backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.human_body);

        backBtn = findViewById(R.id.back_button_body);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    public void OnUpperBackClick(View v) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.upper_back_workouts);

        RecyclerView recyclerView = dialog.findViewById(R.id.upper_back_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayUpperBackWorkouts(recyclerView);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    public void OnLowerBackClick(View v) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.lower_back_workouts);

        RecyclerView recyclerView = dialog.findViewById(R.id.lower_back_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayLowerBackWorkouts(recyclerView);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    public void OnHipClick(View v) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.hip_workouts);

        RecyclerView recyclerView = dialog.findViewById(R.id.hip_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayHipWorkouts(recyclerView);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }


    public void OnHamstringClick(View v) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.hamstring_workouts);

        RecyclerView recyclerView = dialog.findViewById(R.id.hamstring_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayHamstringWorkouts(recyclerView);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    public void OnCalfClick(View v) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.calf_workouts);

        RecyclerView recyclerView = dialog.findViewById(R.id.calf_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayCalfWorkouts(recyclerView);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    public void OnTricepClick(View v) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.tricep_workouts);

        RecyclerView recyclerView = dialog.findViewById(R.id.tricep_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        displayTricepWorkouts(recyclerView);

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

    public void displayUpperBackWorkouts(RecyclerView recyclerView) {
        List<WorkoutBody> workoutBodyList = new ArrayList<>();

        String pull_ups = getResources().getString(R.string.pull_up);
        String db_bentover_row = getResources().getString(R.string.bent_over_row);
        String renegade_rows = getResources().getString(R.string.renegade_rows);
        String pullover = getResources().getString(R.string.pullover);
        String db_shrugs = getResources().getString(R.string.dumbbell_shrugs);

        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Pull Ups")
                .setWorkoutDesc(pull_ups)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Bent Over Row")
                .setWorkoutDesc(db_bentover_row)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Renegade Rows")
                .setWorkoutDesc(renegade_rows)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("DB Pullover")
                .setWorkoutDesc(pullover)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("DB Shrugs")
                .setWorkoutDesc(db_shrugs)
                .build());

        recyclerView.setAdapter(new WorkoutBodyAdapter(workoutBodyList));
    }

    public void displayLowerBackWorkouts(RecyclerView recyclerView) {
        List<WorkoutBody> workoutBodyList = new ArrayList<>();

        String deadlift = getResources().getString(R.string.deadlifts);
        String supermans = getResources().getString(R.string.supermans);
        String good_mornings = getResources().getString(R.string.good_mornings);

        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Deadlifts")
                .setWorkoutDesc(deadlift)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Supermans")
                .setWorkoutDesc(supermans)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Good Mornings")
                .setWorkoutDesc(good_mornings)
                .build());

        recyclerView.setAdapter(new WorkoutBodyAdapter(workoutBodyList));
    }

    public void displayHipWorkouts(RecyclerView recyclerView) {
        List<WorkoutBody> workoutBodyList = new ArrayList<>();

        String glute_bridges = getResources().getString(R.string.glute_bridges);
        String side_leg_raises = getResources().getString(R.string.side_leg_raises);
        String clamshells = getResources().getString(R.string.clamshells);

        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Glute Bridges")
                .setWorkoutDesc(glute_bridges)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Side Leg Raises")
                .setWorkoutDesc(side_leg_raises)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Clamshells")
                .setWorkoutDesc(clamshells)
                .build());

        recyclerView.setAdapter(new WorkoutBodyAdapter(workoutBodyList));
    }


    public void displayHamstringWorkouts(RecyclerView recyclerView) {
        List<WorkoutBody> workoutBodyList = new ArrayList<>();

        String rdl = getResources().getString(R.string.romanian_deadlifts);
        String hamstring_curls = getResources().getString(R.string.hamstring_curls);
        String nordic_holds = getResources().getString(R.string.nordic_holds);

        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Romanian Deadlifts")
                .setWorkoutDesc(rdl)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Hamstring Curls")
                .setWorkoutDesc(hamstring_curls)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Nordic Holds")
                .setWorkoutDesc(nordic_holds)
                .build());

        recyclerView.setAdapter(new WorkoutBodyAdapter(workoutBodyList));
    }

    public void displayCalfWorkouts(RecyclerView recyclerView) {
        List<WorkoutBody> workoutBodyList = new ArrayList<>();

        String jump_ropes = getResources().getString(R.string.jump_rope);
        String calf_raises = getResources().getString(R.string.calf_raises);

        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Jump Rope")
                .setWorkoutDesc(jump_ropes)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Calf Raises")
                .setWorkoutDesc(calf_raises)
                .build());

        recyclerView.setAdapter(new WorkoutBodyAdapter(workoutBodyList));
    }

    public void displayTricepWorkouts(RecyclerView recyclerView) {
        List<WorkoutBody> workoutBodyList = new ArrayList<>();

        String dips = getResources().getString(R.string.dips);
        String skull_crushers = getResources().getString(R.string.skull_crushers);
        String close_grip_bench_press = getResources().getString(R.string.close_grip_bench_press);

        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Dips")
                .setWorkoutDesc(dips)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Skull Crushers")
                .setWorkoutDesc(skull_crushers)
                .build());
        workoutBodyList.add(new WorkoutBody.Build()
                .setWorkoutName("Close Grip Bench Press")
                .setWorkoutDesc(close_grip_bench_press)
                .build());

        recyclerView.setAdapter(new WorkoutBodyAdapter(workoutBodyList));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
