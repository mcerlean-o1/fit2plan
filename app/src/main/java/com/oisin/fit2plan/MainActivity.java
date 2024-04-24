package com.oisin.fit2plan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CoreFourView core4view;

    ImageView profilePicture;
    TextView usernameProfile, bmrTxt, tdeeTxt;
    GestureDetector gesture;
    DrawerLayout drawLayout;
    NavigationView navigationView;
    //FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bmrTxt = findViewById(R.id.bmrTxtView);
        tdeeTxt = findViewById(R.id.tdeeTxtView);
        core4view = findViewById(R.id.core_four_view);

        drawLayout = findViewById(R.id.navLayout);

        navigationView = findViewById(R.id.navigationView);


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
                    Glide.with(MainActivity.this).load(profilePictureUrl).into(profilePicture);
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
                startActivity(new Intent(MainActivity.this, Profile.class));

                drawLayout.closeDrawer(GravityCompat.START);
            }
        });
        drawLayout.closeDrawer(GravityCompat.START);
    }






    private void handleNavigationItemClick(int id) {
        Intent intent = null;
        if (id == R.id.navHome) {
            intent = new Intent(MainActivity.this, MainActivity.class);
        } else if (id == R.id.foodDiaryPage) {
            intent = new Intent(MainActivity.this, FoodDiary.class);
        } else if (id == R.id.workoutDiaryPage) {
            intent = new Intent(MainActivity.this, WorkoutDiary.class);
        } else if (id == R.id.chatRoomPage) {
            intent = new Intent(MainActivity.this, Friends.class);
        }

        if (intent != null) {
            startActivity(intent);
            drawLayout.closeDrawer(GravityCompat.START);
        }
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



//    private void updateUI(FirebaseUser user1) {
//        usernameProfile.setText(user1.getDisplayName());
//
//        if (user1.getPhotoUrl() != null) {
//            Glide.with(this).load(user1.getPhotoUrl()).into(profilePicture);
//        }
//
//    }

    public void calculateBMRAndTDEE(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User currentUser = snapshot.getValue(User.class);
                    if (currentUser != null) {
                        bmrTxt.setVisibility(View.VISIBLE);
                        tdeeTxt.setVisibility(View.VISIBLE);
                        double bmr = CalorieCalculator.calculateBMR(currentUser);
                        double tdee = CalorieCalculator.calculateTDEE(currentUser);
                        displayResults(bmr, tdee);
                    } else {
                        bmrTxt.setVisibility(View.GONE);
                        tdeeTxt.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Need Profile to view BMR and TDEE", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void displayResults(double bmr, double tdee) {
        bmrTxt.setText(String.format(Locale.getDefault(), "BMR: \n %.0f cals", bmr));
        tdeeTxt.setText(String.format(Locale.getDefault(), "Recommended Calories Today: \n %.0f cals", tdee));
        Log.d("MainActivity", "BMR: " + bmr + " TDEE: " + tdee);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


}