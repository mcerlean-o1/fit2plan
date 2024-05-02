package com.oisin.fit2plan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class EditProfile extends AppCompatActivity {

    private EditText editUsername, editEmail, editAge, editWeight, editHeight;
    private Spinner editGender, editActivityLevel;
    private Button editProfileButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);


        editUsername= findViewById(R.id.edtUsername);
        editEmail = findViewById(R.id.edtEmail);
        editAge = findViewById(R.id.edtAge);
        editWeight = findViewById(R.id.edtWeight);
        editHeight = findViewById(R.id.edtHeight);
        editGender = findViewById(R.id.edtGender);
        editActivityLevel = findViewById(R.id.edtActivity);

        editProfileButton = findViewById(R.id.btnEditProfile);


        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(EditProfile.this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(EditProfile.this,
                R.array.activity_level_array, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        editGender.setAdapter(genderAdapter);
        editActivityLevel.setAdapter(activityAdapter);


        loadUser();

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
                startActivity(new Intent(EditProfile.this, Profile.class));
                Toast.makeText(EditProfile.this, "User Details Saved.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String id = user.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(id);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User currentUser = snapshot.getValue(User.class);
                    if (currentUser != null) {
                        editUsername.setText(currentUser.getUsername());
                        editEmail.setText(currentUser.getEmail());
                        editAge.setText(String.valueOf(currentUser.getAge()));
                        editWeight.setText(String.format("%.2f", currentUser.getWeight()));
                        editHeight.setText(String.format("%.2f", currentUser.getHeight()));

                        String[] genders = getResources().getStringArray(R.array.gender_array);
                        int genderIndex = Arrays.asList(genders).indexOf(currentUser.getGender());
                        if (genderIndex >= 0) {
                            editGender.setSelection(genderIndex);
                        }

                        String[] activityLevels = getResources().getStringArray(R.array.activity_level_array);
                        int activityIndex = Arrays.asList(activityLevels).indexOf(currentUser.getActivityLevel());
                        if (activityIndex >= 0) {
                            editActivityLevel.setSelection(activityIndex);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("Profile", "loadUser:onCancelled", error.toException());
                }
            });
        }
    }

    private void saveUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String id = user.getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(id);

            String editedUsername = editUsername.getText().toString().trim();
            String editedEmail = editEmail.getText().toString().trim();
            int editedAge = Integer.parseInt(editAge.getText().toString().trim());
            double editedWeight = Double.parseDouble(editWeight.getText().toString().trim());
            double editedHeight = Double.parseDouble(editHeight.getText().toString().trim());
            String editedGender = editGender.getSelectedItem().toString();
            String editedActivityLevel = editActivityLevel.getSelectedItem().toString();

            User editedUser = new User();
            editedUser.setUsername(editedUsername);
            editedUser.setEmail(editedEmail);
            editedUser.setAge(editedAge);
            editedUser.setWeight(editedWeight);
            editedUser.setHeight(editedHeight);
            editedUser.setGender(editedGender);
            editedUser.setActivityLevel(editedActivityLevel);

            reference.setValue(editedUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditProfile.this, "Updated Profile", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProfile.this, "Failed to make changes", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            if (!editedEmail.equals(user.getEmail())) {
                user.updateEmail(editedEmail);
            }

        }
    }
}
