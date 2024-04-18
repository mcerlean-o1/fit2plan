package com.oisin.fit2plan;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FoodDiary extends AppCompatActivity {

    ImageButton addFood;
    TextView dateText;
    ArrayList<Food> foods;
    RecyclerView recyclerView;
    FoodAdapter foodAdapter;
    Button prevDay, nextDay;

    private Uri imageUri;
    private String currentPhotoPath;
    private long currentMealId = -1;

    private static final int CAMERA_PERMISSIONS_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_diary);
        addFood = findViewById(R.id.food_add_button);


        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
            Log.e("UncaughtException", "Uncaught exception in thread: " + thread.getName(), e);
        });



        //Set date to display
        dateText = findViewById(R.id.txt_date);
        String currentDate = getCurrentDate();
        dateText.setText(currentDate);

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) FoodDiary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.food_input,null,false);
                Spinner mealTypeSpinner = viewInput.findViewById(R.id.meal_type);
                EditText mealDescription = viewInput.findViewById(R.id.edt_meal_description);

                Button camera = viewInput.findViewById(R.id.camera_button1);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FoodDiary.this,
                        R.array.meal_types_array, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mealTypeSpinner.setAdapter(adapter);

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            takePhoto();
                        } else {
                            String[] permissions = {Manifest.permission.CAMERA};
                            requestPermissions(permissions, CAMERA_PERMISSIONS_CODE);
                            Toast.makeText(FoodDiary.this, "Please Enable Camera Permissions", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                new AlertDialog.Builder(FoodDiary.this)
                        .setView(viewInput)
                        .setTitle("Add Meal")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String date = getCurrentDate();
                                String mealDesc = mealDescription.getText().toString();
                                String mealType = mealTypeSpinner.getSelectedItem().toString();


                                Food food = new Food(date, mealType, mealDesc);

                                long isInserted = new FoodHandler(FoodDiary.this).create(food);

                                if (isInserted != -1) {
                                    currentMealId = isInserted;
                                    Toast.makeText(FoodDiary.this, "Entry Saved", Toast.LENGTH_SHORT).show();
                                    loadFoods();
                                } else {
                                    Toast.makeText(FoodDiary.this, "Unable to save entry", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("Cancel", null)
                        .show();

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
                new FoodHandler(FoodDiary.this).delete(foods.get(viewHolder.getAdapterPosition()).getId());
                foods.remove(viewHolder.getAdapterPosition());
                foodAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadFoods();

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

    public ArrayList<Food> readFoods(){
        ArrayList<Food> foods = new FoodHandler(this).readFoods();
        return foods;
    }

    public void loadFoods(){
        foods = readFoods();
        foodAdapter = new FoodAdapter(foods, this, new FoodAdapter.ItemClicked() {
            @Override
            public void onClick(int position, View view) {
                editFood(foods.get(position).getId(),view);
            }
        });
        recyclerView.setAdapter(foodAdapter);

    }

    private void editFood(int foodId, View view){
        FoodHandler foodHandler =new FoodHandler(this);
        Food food = foodHandler.readSingleFood(foodId);
        Intent intent = new Intent(this,EditFood.class);
        intent.putExtra("date", food.getDate());
        intent.putExtra("mealType", food.getMealType());
        intent.putExtra("mealDescription", food.getDescription());
        intent.putExtra("id", food.getId());

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        startActivityForResult(intent,1,optionsCompat.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Integer.parseInt("100") && resultCode == RESULT_OK) {
            if (currentPhotoPath != null) {
                new FoodHandler(this).addPhoto(currentMealId, currentPhotoPath);
            }
        }

        if (requestCode == 1) {
            loadFoods();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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
        ArrayList<Food> foodsForDate = new ArrayList<>();
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

    private void takePhoto() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Unable to create Image file", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null){
                imageUri = FileProvider.getUriForFile(this,
                        "com.oisin.fit2plan.fileprovider", photoFile);

                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(pictureIntent, Integer.parseInt("100"));
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}

