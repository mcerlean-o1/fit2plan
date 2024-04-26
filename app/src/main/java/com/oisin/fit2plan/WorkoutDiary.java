package com.oisin.fit2plan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WorkoutDiary extends AppCompatActivity {

    ImageButton workout_button, workout_body_button;
    TextView dateTxt;
    ArrayList<Workout> workouts;
    RecyclerView recyclerView;
    WorkoutAdapter workoutAdapter;
    Button prevDay, nextDay;
    ImageView profilePicture;
    TextView usernameProfile;
    GestureDetector gesture;
    DrawerLayout drawLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_diary);
        workout_button = findViewById(R.id.workout_add_button);
        workout_body_button = findViewById(R.id.workout_body_button);

        dateTxt = findViewById(R.id.txt_date_workout);


        String currentDate = getCurrentDate();
        dateTxt.setText(currentDate);

        drawLayout = findViewById(R.id.navWorkoutLayout);

        navigationView = findViewById(R.id.navigationWorkoutView);


        View headerView = navigationView.getHeaderView(0);
        profilePicture = headerView.findViewById(R.id.profilePic);
        usernameProfile = headerView.findViewById(R.id.proUsername);


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            handleNavigationItemClick(id);
            return true;
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.child("profilePicture").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profilePictureUrl = snapshot.getValue(String.class);
                if (profilePictureUrl != null) {
                    Glide.with(WorkoutDiary.this).load(profilePictureUrl).into(profilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.w("Activity Error", "loadPost:onCancelled", error.toException() );
            }
        });

        databaseReference.child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usernameData = snapshot.getValue(String.class);
                if (usernameData != null) {
                    usernameProfile.setText(usernameData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.w("Activity Error", "loadPost:onCancelled", error.toException() );
            }
        });

        setUpGestureHandler();

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkoutDiary.this, Profile.class));

                drawLayout.closeDrawer(GravityCompat.START);
            }
        });
        drawLayout.closeDrawer(GravityCompat.START);

        workout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) WorkoutDiary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.workout_input,null,false);
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
                                String date =  dateTxt.getText().toString();
                                String a1 =  edtA1.getText().toString();
                                String a2 =  edtA2.getText().toString();
                                String b1 =  edtB1.getText().toString();
                                String b2 =  edtB2.getText().toString();
                                String c1 =  edtC1.getText().toString();
                                String c2 =  edtC2.getText().toString();
                                String multiNotes =  edtMultiNotes.getText().toString();

                                Workout workout = new Workout(-1, date,a1,a2,b1,b2,c1,c2,multiNotes);

                                long isInserted = new WorkoutHandler(WorkoutDiary.this).create(workout);

                                if (isInserted != -1){
                                    workout.setId((int) isInserted);
                                    Toast.makeText(WorkoutDiary.this, "Entry Saved", Toast.LENGTH_SHORT).show();
                                    loadWorkouts(dateTxt.getText().toString());
                                } else {
                                    Toast.makeText(WorkoutDiary.this, "Unable to save entry", Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                            }
                        }).show();

            }
        });

        workout_body_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkoutDiary.this, InteractiveBody.class));
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
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && position < workouts.size()) {
                    if (new WorkoutHandler(WorkoutDiary.this).delete(workouts.get(position).getId())) {
                        workouts.remove(position);
                        workoutAdapter.notifyItemRemoved(position);
                        workoutAdapter.notifyItemRangeChanged(position, workouts.size());
                        Toast.makeText(WorkoutDiary.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(WorkoutDiary.this, "Failed to delete workout", Toast.LENGTH_SHORT).show();
                        workoutAdapter.notifyItemChanged(position);
                    }
                } else {
                    Log.e("WorkoutDiary", "Workout list is null or invalid");
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadWorkouts(dateTxt.getText().toString());


        prevDay = findViewById(R.id.previous_day_workout);
        nextDay = findViewById(R.id.next_day_workout);

        prevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDate = getPreviousDate();
                updateDateAndWorkout(newDate);
                dateTxt.setText(newDate);
            }
        });

        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDate = getNextDate();
                updateDateAndWorkout(newDate);
                dateTxt.setText(newDate);
            }
        });
    }

    public void loadWorkouts(String date){
        workouts = readWorkoutsforDate(date);
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
        intent.putExtra("dates", workout.getDates());
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
            loadWorkouts(dateTxt.getText().toString());

        }
    }

    private String getPreviousDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            Date date = dateFormat.parse(dateTxt.getText().toString());
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        } catch (ParseException e) {
            Log.e("Workout Diary", "Error parsing date", e);
            //throw new RuntimeException(e);
        }
        String newDate = dateFormat.format(calendar.getTime());
        return newDate;

    }

    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private void updateDateAndWorkout(String newDate) {
        dateTxt.setText(newDate);
        loadWorkoutsforDate(newDate);
    }

    private void loadWorkoutsforDate(String date) {
        ArrayList<Workout> newWorkouts = readWorkoutsforDate(date);
        workoutAdapter.updateWorkouts(newWorkouts);
    }

    private ArrayList<Workout> readWorkoutsforDate(String date) {
        ArrayList<Workout> workoutsForDate;
        WorkoutHandler workoutHandler = new WorkoutHandler(this);


        workoutsForDate = workoutHandler.readWorkoutsByDate(date);
        return workoutsForDate;
    }

    private String getNextDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            Date date = dateFormat.parse(dateTxt.getText().toString());
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, +1);
        } catch (ParseException e) {
            Log.e("Workout Diary", "Error parsing date", e);
            //throw new RuntimeException(e);
        }

        String newDate = dateFormat.format(calendar.getTime());
        return newDate;

    }

    public void setUpGestureHandler() {

        gesture = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent e) {

            }

            @Override
            public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                if (e2.getX() - e1.getX() > 100) { //swipe left to right
                    drawLayout.openDrawer(GravityCompat.START);
                    return true;
                } else if (e1.getX() - e2.getX() > 100) {
                    if (drawLayout.isDrawerOpen(GravityCompat.START)) {
                        drawLayout.closeDrawer(GravityCompat.START);
                    }
                }
                return false;
            }
        });
    }

    private void handleNavigationItemClick(int id) {
        Intent intent = null;
        if (id == R.id.navHome) {
            intent = new Intent(WorkoutDiary.this, MainActivity.class);
        } else if (id == R.id.foodDiaryPage) {
            intent = new Intent(WorkoutDiary.this, FoodDiary.class);
        } else if (id == R.id.workoutDiaryPage) {
            intent = new Intent(WorkoutDiary.this, WorkoutDiary.class);
        } else if (id == R.id.chatRoomPage) {
            intent = new Intent(WorkoutDiary.this, Friends.class);
        } else if (id == R.id.nutritionCalculatorPage) {
            intent = new Intent(WorkoutDiary.this, NutritionCalculator.class);
        }

        if (intent != null) {
            startActivity(intent);
            drawLayout.closeDrawer(GravityCompat.START);
        }
    }

}

