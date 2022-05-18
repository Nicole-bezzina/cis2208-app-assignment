package com.example.shoppingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialising Firebase Auth
        auth = FirebaseAuth.getInstance();

        //linking the view from the layout resource xml file to its activity class
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

    }

    public void signup(View view) {
        //invoking register activity
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void login(View view) {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        //user input validation methods
        if  (TextUtils.isEmpty(userEmail)){
            Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_LONG).show();
            return;
        }
        else if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            //validating the email address and password of an existing user from firebase
            //and if found,  the user is signed in
            auth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Error: " + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}