package com.oisin.fit2plan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class NutritionCalculator extends AppCompatActivity {

    private TextInputEditText age, weight, height;
    private Spinner gender, activityLevel;
    ImageView profilePicture;
    Button calculate;
    GestureDetector gesture;
    DrawerLayout drawLayout;
    NavigationView navigationView;
    TextView bmrTxt, tdeeTxt, proteinTxt, deficitTxt, surplusTxt,
            usernameProfile, tdeeResult, proteinResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_calculator);

        bmrTxt = findViewById(R.id.bmr);
        tdeeTxt = findViewById(R.id.tdee);
        proteinTxt = findViewById(R.id.protein);
        deficitTxt = findViewById(R.id.deficit);
        surplusTxt = findViewById(R.id.surplus);

        age = findViewById(R.id.edtAge);
        weight = findViewById(R.id.edtWeight);
        height = findViewById(R.id.edtHeight);
        gender = findViewById(R.id.edtGender);
        activityLevel = findViewById(R.id.edtActivity);

        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(NutritionCalculator.this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(NutritionCalculator.this,
                R.array.activity_level_array, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityLevel.setAdapter(activityAdapter);

        tdeeResult = findViewById(R.id.tdeeResult);
        proteinResult = findViewById(R.id.proteinResult);

        calculate = findViewById(R.id.btnCalculate);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateInputNutrition();
            }
        });


        calculateNutritionalResults();

        navigationView = findViewById(R.id.navigationNutritionView);
        drawLayout = findViewById(R.id.drawNutritionLayout);

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
                    Glide.with(NutritionCalculator.this).load(profilePictureUrl).into(profilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.w("Activity Error", "loadPost:onCancelled", error.toException());
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

                Log.w("Activity Error", "loadPost:onCancelled", error.toException());
            }
        });

        setUpGestureHandler();

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NutritionCalculator.this, Profile.class));

                drawLayout.closeDrawer(GravityCompat.START);
            }
        });
        drawLayout.closeDrawer(GravityCompat.START);
    }

    private void handleNavigationItemClick(int id) {
        Intent intent = null;
        if (id == R.id.navHome) {
            intent = new Intent(NutritionCalculator.this, MainActivity.class);
        } else if (id == R.id.foodDiaryPage) {
            intent = new Intent(NutritionCalculator.this, FoodDiary.class);
        } else if (id == R.id.workoutDiaryPage) {
            intent = new Intent(NutritionCalculator.this, WorkoutDiary.class);
        } else if (id == R.id.chatRoomPage) {
            intent = new Intent(NutritionCalculator.this, Friends.class);
        } else if (id == R.id.nutritionCalculatorPage) {
            intent = new Intent(NutritionCalculator.this, NutritionCalculator.class);
        }

        if (intent != null) {
            startActivity(intent);
            drawLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void calculateInputNutrition() {
        String ageString = age.getText().toString();
        String weightString = weight.getText().toString();
        String heightString = height.getText().toString();

        int ageInput = Integer.parseInt(ageString);
        double weightInput = Double.parseDouble(weightString);
        double heightInput = Double.parseDouble(heightString);
        String genderResult = gender.getSelectedItem().toString();
        String activityResult = activityLevel.getSelectedItem().toString();

        Nutrition nutrition = new Nutrition(ageInput, weightInput, heightInput, genderResult, activityResult);
        double tdeeResult = CalorieCalculator.calculateTDEEInput(nutrition);
        double proteinResult = CalorieCalculator.calculateProtein(weightInput, activityResult);
        displayInputResults(tdeeResult, proteinResult);
    }

    public void displayInputResults(double tdee, double protein) {
        tdeeResult.setText(String.format(Locale.getDefault(), "Maintenance Calories: \n%.0f cals", tdee));
        proteinResult.setText(String.format(Locale.getDefault(), "Protein: \n%.0fg", protein));
    }

    public void calculateNutritionalResults() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User currentUser = snapshot.getValue(User.class);
                    if (currentUser != null) {
                        double bmr = CalorieCalculator.calculateBMR(currentUser);
                        double tdee = CalorieCalculator.calculateTDEE(currentUser);
                        double protein = CalorieCalculator.calculateProtein(currentUser.getWeight(), currentUser.getActivityLevel());
                        double deficit = CalorieCalculator.calculateDeficit(currentUser);
                        double surplus = CalorieCalculator.calculateSurplus(currentUser);
                        displayResults(bmr, tdee, protein, deficit, surplus);
                    } else {
                        Toast.makeText(NutritionCalculator.this, "Need Profile to view BMR and TDEE", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Toast.makeText(this, "Need to login", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayResults(double bmr, double tdee, double protein, double deficit, double surplus) {
        bmrTxt.setText(String.format(Locale.getDefault(), "%.0f cals", bmr));
        tdeeTxt.setText(String.format(Locale.getDefault(), "%.0f cals", tdee));
        proteinTxt.setText(String.format(Locale.getDefault(), "%.0fg", protein));
        deficitTxt.setText(String.format(Locale.getDefault(), "%.0f cals", deficit));
        surplusTxt.setText(String.format(Locale.getDefault(), "%.0f cals", surplus));
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
}