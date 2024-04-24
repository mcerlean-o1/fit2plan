package com.oisin.fit2plan;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditFood extends AppCompatActivity {


    Button edtCancel, edtSave;
    EditText edtDate, edtMealDescription;
    Spinner edtMealType;
    LinearLayout linearLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        Intent intent = getIntent();
        linearLayout = findViewById(R.id.btn_holder);
        edtDate = findViewById(R.id.edt_edit_date);
        edtMealType = findViewById(R.id.edt_edit_mealType);
        edtMealDescription = findViewById(R.id.edt_edit_meal_description);

        String dateCurrent = intent.getStringExtra("date");
        String currentMealType = intent.getStringExtra("mealType");
        String currentMealDescription = intent.getStringExtra("mealDescription");
        int currentId = intent.getIntExtra("id", -1);

        edtDate.setText(dateCurrent != null ? dateCurrent : "");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditFood.this,
                R.array.meal_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtMealType.setAdapter(adapter);

        edtMealDescription.setText(currentMealDescription != null ? currentMealDescription : "");

        if (currentMealType != null && currentMealDescription != null && currentId != -1) {
            Food food = new Food(currentId, dateCurrent, currentMealType, currentMealDescription);
            food.setId(currentId);
            int spinnerPosition = adapter.getPosition(currentMealType);
            edtMealType.setSelection(spinnerPosition);
        } else {
            Toast.makeText(this, "Food data needs completed", Toast.LENGTH_SHORT).show();
            finish();
        }






        edtCancel = findViewById(R.id.cancel_button);
        edtSave = findViewById(R.id.save_button);

        edtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        edtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = currentId;
                String date = edtDate.getText().toString();
                String mealType = edtMealType.getSelectedItem().toString();
                String mealDescription = edtMealDescription.getText().toString();

                Food food = new Food(id, date, mealType, mealDescription);
                food.setId(intent.getIntExtra("id", -1));

                if (new FoodHandler(EditFood.this).update(food)) {
                    Toast.makeText(EditFood.this, "Changes saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditFood.this, "Unable to make changes", Toast.LENGTH_SHORT).show();
                }

                onBackPressed();
            }

        });








    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        edtSave.setVisibility(View.GONE);
        edtCancel.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(linearLayout);
    }
}