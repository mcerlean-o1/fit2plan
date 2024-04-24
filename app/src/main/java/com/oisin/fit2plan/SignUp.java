package com.oisin.fit2plan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private TextInputLayout usernameLayout, ageLayout, weightLayout, heightLayout, genderLayout, activityLayout;
    private TextInputEditText username, email, password, age, weight, height;
    private Spinner gender, activityLevel;
    private Button register;

    private TextView login;

    private boolean isSigningUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.edtUsername);
        email = findViewById(R.id.edtEmail);
        password = findViewById(R.id.edtPassword);
        age = findViewById(R.id.edtAge);
        weight = findViewById(R.id.edtWeight);
        height = findViewById(R.id.edtHeight);
        gender = findViewById(R.id.edtGender);
        activityLevel = findViewById(R.id.edtActivity);

        usernameLayout = findViewById(R.id.textInputLayout3);
        ageLayout = findViewById(R.id.ageLayout);
        weightLayout = findViewById(R.id.weightLayout);
        heightLayout = findViewById(R.id.heightLayout);
        genderLayout = findViewById(R.id.genderLayout);
        activityLayout = findViewById(R.id.activityLayout);

        register = findViewById(R.id.btnRegister);
        login = findViewById(R.id.btnLogin);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(SignUp.this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(SignUp.this,
                R.array.activity_level_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityLevel.setAdapter(adapter1);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(SignUp.this, MainActivity.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Log.d("SignUp", "Creating: " + username + age + height + gender);
                    if (isSigningUp && username.getText().toString().isEmpty()
                            || age.getText().toString().isEmpty()
                            || weight.getText().toString().isEmpty()
                            || height.getText().toString().isEmpty() ) {
                        Toast.makeText(SignUp.this, "Invalid input", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (isSigningUp) {
                    handleSignUp();
                } else {
                    handleLogin();
                }
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSigningUp) {
                    isSigningUp = false;
                    usernameLayout.setVisibility(View.GONE);
                    ageLayout.setVisibility(View.GONE);
                    weightLayout.setVisibility(View.GONE);
                    heightLayout.setVisibility(View.GONE);
                    genderLayout.setVisibility(View.GONE);
                    activityLayout.setVisibility(View.GONE);
                    register.setText("Log in");
                    login.setText("Don't have an account? Sign up");
                } else {
                    isSigningUp = true;
                    usernameLayout.setVisibility(View.VISIBLE);
                    ageLayout.setVisibility(View.VISIBLE);
                    weightLayout.setVisibility(View.VISIBLE);
                    heightLayout.setVisibility(View.VISIBLE);
                    genderLayout.setVisibility(View.VISIBLE);
                    activityLayout.setVisibility(View.VISIBLE);
                    register.setText("Sign up");
                    login.setText("Already Registered? Log in");
                }
            }
        });


    }

    private void handleSignUp() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),
                password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User newUser = new User(username.getText().toString(),
                            email.getText().toString(),
                            "",
                            Integer.parseInt(age.getText().toString()),
                            Double.parseDouble(weight.getText().toString()),
                            Double.parseDouble(height.getText().toString()),
                            gender.getSelectedItem().toString(),
                            activityLevel.getSelectedItem().toString());
                    FirebaseDatabase.getInstance().getReference("users/" +FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newUser);
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                    Toast.makeText(SignUp.this, "Account Successfully Registered", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUp.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void handleLogin() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                    Toast.makeText(SignUp.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUp.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}