package com.oisin.fit2plan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnFoodDiary, btnWorkoutDiary;
    CoreFourView core4view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        core4view = findViewById(R.id.core_four_view);

        btnFoodDiary = findViewById(R.id.btnFoodDiary);
        btnWorkoutDiary = findViewById(R.id.btnWorkoutDiary);

        btnFoodDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FoodDiary.class));
            }
        });

        btnWorkoutDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WorkoutDiary.class));
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}