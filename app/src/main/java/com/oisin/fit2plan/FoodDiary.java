package com.oisin.fit2plan;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FoodDiary extends AppCompatActivity implements FoodAdapter.OnPhotoClickListener{

    ImageButton addFood;
    ImageView imageView;
    TextView dateText;
    ArrayList<Food> foods;
    RecyclerView recyclerView;
    FoodAdapter foodAdapter;
    FoodHandler foodHandler;
    Button prevDay, nextDay, backBtn;
    private Handler handler;
    private Dialog dialog;
    private Uri imageUri;
    private String currentPhotoPath;
    private long currentMealId = -1;

    private static final int CAMERA_PERMISSIONS_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_diary);

        foodHandler = new FoodHandler(this);
        addFood = findViewById(R.id.food_add_button);

//        Attaches handler to the thread's main looper
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing() && !isDestroyed()) {
                    Toast.makeText(FoodDiary.this, "Task Delayed", Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000);

        dialog = new Dialog(this);


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
                                    currentMealId = isInserted;
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

        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
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
                } else if (direction == ItemTouchHelper.RIGHT) {
                    showPhotosForFood(position);
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
        foodAdapter = new FoodAdapter(foods, this, listener,this);
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
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Log.d("FoodDiary", "Photo Result Received, Current meal ID: " + currentMealId);
            if (currentPhotoPath != null && currentMealId != -1) {
                Photo photo = new Photo(-1, (int) currentMealId, currentPhotoPath);
                boolean photoAdded = new FoodHandler(this).addPhoto(photo);
                if (!photoAdded) {
                    Log.e("FoodHandler", "Failed to add photo for mealID: " + currentMealId);
                } else {
                    Log.i("FoodDiary", "Photo path added successfully.");
                }

            } else {
                Log.e("FoodHandler", "Failed to add photo for mealID: " + currentMealId);
            }
        }

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
                Log.d("Food Diary", "Photo taken: " + photoFile);
                imageUri = FileProvider.getUriForFile(this,
                        "com.oisin.fit2plan.file-provider", photoFile);

                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(pictureIntent, 100);
            } else {
                Log.e("FoodDiary", "Photo null" );
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void refreshFood() {
        ArrayList<Food> updatedFood = readFoodsforDate(getCurrentDate());
        if (foodAdapter != null) {
            foodAdapter.updateFoods(updatedFood);
            foodAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTakePhoto(int mealId) {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            currentMealId = mealId;
            takePhoto();
        } else {
            String [] permission = new String[]{Manifest.permission.CAMERA};
            requestPermissions(permission, CAMERA_PERMISSIONS_CODE);
            Toast.makeText(FoodDiary.this, "Please Enable Camera Permissions", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPhotosForFood(int foodId) {
        List<Photo> photos = foodHandler.getPhotos(foodId);
        if (!photos.isEmpty()) {
            showPhoto(photos.get(0).getPhotoPath());
        } else {
            Toast.makeText(this, "No Photos logged for this meal.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPhoto(String photoPath) {
        if (photoPath != null) {
            Log.e("showPhoto", "Photo path is null");
        }
        Dialog photoDialog = new Dialog(this);
        photoDialog.setContentView(R.layout.image_view);

        imageView = findViewById(R.id.imageView);
        backBtn = findViewById(R.id.back_button);

        if (imageView == null) {
            Log.e("showPhoto", "ImageView not found");
            return;
        }

        Glide.with(this).load(photoPath).into(imageView);
        backBtn.setOnClickListener(v -> {
            photoDialog.dismiss();
        });
        photoDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog !=null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshFood();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}

