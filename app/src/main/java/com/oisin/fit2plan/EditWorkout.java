package com.oisin.fit2plan;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditWorkout extends AppCompatActivity {


    Button edtCancel, edtSave;
    EditText edtDate, edtA1, edtA2, edtB1, edtB2, edtC1, edtC2, edtMultiNotes;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_workout);

        Intent intent = getIntent();
        linearLayout = findViewById(R.id.btn_holder);
        edtDate = findViewById(R.id.edt_edit_date);
        edtA1 = findViewById(R.id.edt_edit_a1);
        edtA2 = findViewById(R.id.edt_edit_a2);
        edtB1 = findViewById(R.id.edt_edit_b1);
        edtB2 = findViewById(R.id.edt_edit_b2);
        edtC1 = findViewById(R.id.edt_edit_c1);
        edtC2 = findViewById(R.id.edt_edit_c2);
        edtMultiNotes = findViewById(R.id.edt_edit_multiNotes);

        edtCancel = findViewById(R.id.cancel_button);
        edtSave = findViewById(R.id.save_button);

        edtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                edtSave.setVisibility(View.GONE);
                edtCancel.setVisibility(View.GONE);
                TransitionManager.beginDelayedTransition(linearLayout);

            }
        });

        edtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Workout workout = new Workout(edtDate.getText().toString(),
                        edtA1.getText().toString(),
                        edtA2.getText().toString(),
                        edtB1.getText().toString(),
                        edtB2.getText().toString(),
                        edtC1.getText().toString(),
                        edtC2.getText().toString(),
                        edtMultiNotes.getText().toString());
                workout.setId(intent.getIntExtra("id",1));
                if (new WorkoutHandler(EditWorkout.this).update(workout)){
                    Toast.makeText(EditWorkout.this, "Changes saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditWorkout.this, "Unable to make changes", Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
            }

        });


        edtDate.setText(intent.getStringExtra("date"));
        edtA1.setText(intent.getStringExtra("a1"));
        edtA2.setText(intent.getStringExtra("a2"));
        edtB1.setText(intent.getStringExtra("b1"));
        edtB2.setText(intent.getStringExtra("b2"));
        edtC1.setText(intent.getStringExtra("c1"));
        edtC2.setText(intent.getStringExtra("c2"));
        edtMultiNotes.setText(intent.getStringExtra("multiNotes"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        edtSave.setVisibility(View.GONE);
        edtCancel.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(linearLayout);
    }
}