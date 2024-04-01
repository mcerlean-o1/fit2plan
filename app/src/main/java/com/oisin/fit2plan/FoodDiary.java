package com.oisin.fit2plan;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodDiary extends AppCompatActivity {

    Button button;
    ArrayList<Food> foods;
    RecyclerView recyclerView;
    FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_diary);
        button = findViewById(R.id.food_add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) FoodDiary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewInput = inflater.inflate(R.layout.food_input,null,false);
                EditText edtDate = viewInput.findViewById(R.id.edt_title);
                EditText edtBreakfast = viewInput.findViewById(R.id.edt_breakfast);
                EditText edtSnack1 = viewInput.findViewById(R.id.edt_snack1);
                EditText edtLunch = viewInput.findViewById(R.id.edt_lunch);
                EditText edtSnack2 = viewInput.findViewById(R.id.edt_snack2);
                EditText edtDinner = viewInput.findViewById(R.id.edt_dinner);



                new AlertDialog.Builder(FoodDiary.this)
                        .setView(viewInput)
                        .setTitle("Add Food")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String date =  edtDate.getText().toString();
                                String breakfast =  edtBreakfast.getText().toString();
                                String snack1 =  edtSnack1.getText().toString();
                                String lunch =  edtLunch.getText().toString();
                                String snack2 =  edtSnack2.getText().toString();
                                String dinner =  edtDinner.getText().toString();

                                Food food = new Food(date,breakfast,snack1,lunch,snack2,dinner);

                                boolean isInserted = new FoodHandler(FoodDiary.this).create(food);
                                
                                if (isInserted){
                                    Toast.makeText(FoodDiary.this, "Entry Saved", Toast.LENGTH_SHORT).show();
                                    loadFoods();
                                } else {
                                    Toast.makeText(FoodDiary.this, "Unable to save entry", Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                            }
                        }).show();

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
        intent.putExtra("breakfast", food.getBreakfast());
        intent.putExtra("snack1", food.getSnack1());
        intent.putExtra("lunch", food.getLunch());
        intent.putExtra("snack2", food.getSnack2());
        intent.putExtra("dinner", food.getDinner());
        intent.putExtra("id", food.getId());

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, ViewCompat.getTransitionName(view));
        startActivityForResult(intent,1,optionsCompat.toBundle());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            loadFoods();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}

