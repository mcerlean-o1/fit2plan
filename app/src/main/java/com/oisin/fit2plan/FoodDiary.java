package com.oisin.fit2plan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class FoodDiary extends AppCompatActivity {

    ImageButton addFood;
    ImageView profilePicture;
    TextView dateText, usernameProfile;
    ArrayList<Food> foods;
    RecyclerView recyclerView;
    GestureDetector gesture;
    DrawerLayout drawLayout;
    NavigationView navigationView;
    FoodAdapter foodAdapter;
    FoodHandler foodHandler;
    Button prevDay, nextDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_diary);

        foodHandler = new FoodHandler(this);
        addFood = findViewById(R.id.food_add_button);


        //Set date to display
        dateText = findViewById(R.id.txt_date);
        String currentDate = getCurrentDate();
        dateText.setText(currentDate);

        drawLayout = findViewById(R.id.navFoodLayout);

        navigationView = findViewById(R.id.navigationFoodView);


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
                    Glide.with(FoodDiary.this).load(profilePictureUrl).into(profilePicture);
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
                startActivity(new Intent(FoodDiary.this, Profile.class));

                drawLayout.closeDrawer(GravityCompat.START);
            }
        });
        drawLayout.closeDrawer(GravityCompat.START);

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) FoodDiary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.food_input, null, false);
                Spinner mealTypeSpinner = viewInput.findViewById(R.id.meal_type);
                EditText mealDescription = viewInput.findViewById(R.id.edt_meal_description);


                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FoodDiary.this,
                        R.array.meal_types_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mealTypeSpinner.setAdapter(adapter);


                new AlertDialog.Builder(FoodDiary.this)
                        .setView(viewInput)
                        .setTitle("Add Meal")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String date = dateText.getText().toString();
                                String mealDesc = mealDescription.getText().toString();
                                String mealType = mealTypeSpinner.getSelectedItem().toString();


                                Food food = new Food(-1, date, mealType, mealDesc);

                                long isInserted = new FoodHandler(FoodDiary.this).create(food);

                                if (isInserted != -1) {
                                    food.setId((int) isInserted);
                                    Log.d("AddedFood", "Food added with Meal ID: " + isInserted);
                                    Toast.makeText(FoodDiary.this, "Entry Saved", Toast.LENGTH_SHORT).show();
                                    loadFoods(dateText.getText().toString());
                                } else {
                                    Toast.makeText(FoodDiary.this, "Unable to save entry", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("Cancel", null)
                        .show();

            }
        });
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && position < foods.size()) {
                    if (new FoodHandler(FoodDiary.this).delete(foods.get(position).getId())) {
                        foods.remove(position);
                        foodAdapter.notifyItemRemoved(position);
                        foodAdapter.notifyItemRangeChanged(position, foods.size());
                    } else {
                        Toast.makeText(FoodDiary.this, "Failed to delete food", Toast.LENGTH_SHORT).show();
                        foodAdapter.notifyItemChanged(position);
                    }
                } else {
                    Log.e("FoodDiary", "Food list is null or invalid");
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadFoods(dateText.getText().toString());
        prevDay = findViewById(R.id.previous_day);
        nextDay = findViewById(R.id.next_day);

        prevDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDate = getPreviousDate();
                updateDateAndFood(newDate);
                dateText.setText(newDate);
            }
        });

        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDate = getNextDate();
                updateDateAndFood(newDate);
                dateText.setText(newDate);
            }
        });
    }

    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FoodAdapter.ItemClicked listener = new FoodAdapter.ItemClicked() {
            @Override
            public void onClick(int position, View view) {
                Food food = foods.get(position);
                editFood(food.getId(), view);
            }

        };
        foodAdapter = new FoodAdapter(foods, this, listener);
        recyclerView.setAdapter(foodAdapter);
    }

    public void loadFoods(String date) {
        ArrayList<Food> newFoods = readFoodsforDate(date);
        if (newFoods == null) {
            newFoods = new ArrayList<>();
        }
        foods = newFoods;

        if (foodAdapter == null) {
            setRecyclerView();
        } else {
            foodAdapter.updateFoods(foods);
        }
    }



    private void editFood(int foodId, View view){
        FoodHandler foodHandler =new FoodHandler(this);
        Food food = foodHandler.readSingleFood(foodId);
        if (food != null) {
            Intent intent = new Intent(FoodDiary.this, EditFood.class);
            intent.putExtra("date", food.getDate());
            intent.putExtra("mealType", food.getMealType());
            intent.putExtra("mealDescription", food.getDescription());
            intent.putExtra("id", food.getId());

            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
            startActivityForResult(intent, 1, optionsCompat.toBundle());

        } else {
            Toast.makeText(this, "Food item not found", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            loadFoods(dateText.getText().toString());

        }
    }

    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private void updateDateAndFood(String newDate) {
        dateText.setText(newDate);
        loadFoodsforDate(newDate);
    }

    private void loadFoodsforDate(String date) {
        ArrayList<Food> newFoods = readFoodsforDate(date);
        foodAdapter.updateFoods(newFoods);
    }

    private ArrayList<Food> readFoodsforDate(String date) {
        ArrayList<Food> foodsForDate;
        FoodHandler foodHandler = new FoodHandler(this);


        foodsForDate = foodHandler.readFoodsByDate(date);
        return foodsForDate;
    }

    private String getPreviousDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            Date date = dateFormat.parse(dateText.getText().toString());
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        } catch (ParseException e) {
            Log.e("Food Diary", "Error parsing date", e);
            //throw new RuntimeException(e);
        }
        String newDate = dateFormat.format(calendar.getTime());
        Log.d("Food Diary", "Previous Date: " + newDate);
        return newDate;

    }

    private String getNextDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            Date date = dateFormat.parse(dateText.getText().toString());
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, +1);
        } catch (ParseException e) {
            Log.e("Food Diary", "Error parsing date", e);
            //throw new RuntimeException(e);
        }

        String newDate = dateFormat.format(calendar.getTime());
        Log.d("Food Diary", "Next Date: " + newDate);
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

    public void refreshFood() {
        ArrayList<Food> updatedFood = readFoodsforDate(getCurrentDate());
        if (foodAdapter != null) {
            foodAdapter.updateFoods(updatedFood);
            foodAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshFood();
    }

    private void handleNavigationItemClick(int id) {
        Intent intent = null;
        if (id == R.id.navHome) {
            intent = new Intent(FoodDiary.this, MainActivity.class);
        } else if (id == R.id.foodDiaryPage) {
            intent = new Intent(FoodDiary.this, FoodDiary.class);
        } else if (id == R.id.workoutDiaryPage) {
            intent = new Intent(FoodDiary.this, WorkoutDiary.class);
        } else if (id == R.id.chatRoomPage) {
            intent = new Intent(FoodDiary.this, Friends.class);
        } else if (id == R.id.nutritionCalculatorPage) {
            intent = new Intent(FoodDiary.this, NutritionCalculator.class);
        }

        if (intent != null) {
            startActivity(intent);
            drawLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}

