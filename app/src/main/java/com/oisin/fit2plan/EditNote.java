package com.oisin.fit2plan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditNote extends AppCompatActivity {


    Button edtCancel, edtSave;
    EditText edtTitle, edtBreakfast, edtSnack1, edtLunch, edtSnack2, edtDinner;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();
        linearLayout = findViewById(R.id.btn_holder);
        edtTitle = findViewById(R.id.edt_edit_title);
        edtBreakfast = findViewById(R.id.edt_edit_breakfast);
        edtSnack1 = findViewById(R.id.edt_edit_snack1);
        edtLunch = findViewById(R.id.edt_edit_lunch);
        edtSnack2 = findViewById(R.id.edt_edit_snack2);
        edtDinner = findViewById(R.id.edt_edit_dinner);

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
                Note note = new Note(edtTitle.getText().toString(),
                        edtBreakfast.getText().toString(),
                        edtSnack1.getText().toString(),
                        edtLunch.getText().toString(),
                        edtSnack2.getText().toString(),
                        edtDinner.getText().toString());
                note.setId(intent.getIntExtra("id",1));
                if (new NoteHandler(EditNote.this).update(note)){
                    Toast.makeText(EditNote.this, "Changes saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditNote.this, "Unable to make changes", Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
            }

        });


        edtTitle.setText(intent.getStringExtra("title"));
        edtBreakfast.setText(intent.getStringExtra("breakfast"));
        edtSnack1.setText(intent.getStringExtra("snack1"));
        edtLunch.setText(intent.getStringExtra("lunch"));
        edtSnack2.setText(intent.getStringExtra("snack2"));
        edtDinner.setText(intent.getStringExtra("dinner"));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        edtSave.setVisibility(View.GONE);
        edtCancel.setVisibility(View.GONE);
        TransitionManager.beginDelayedTransition(linearLayout);
    }
}