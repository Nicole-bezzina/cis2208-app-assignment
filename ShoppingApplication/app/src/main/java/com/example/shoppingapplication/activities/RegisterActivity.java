package com.example.shoppingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private CheckBox CheckBoxTerms;
    private Button register;
    EditText name, email,password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialising Firebase Auth
        auth = FirebaseAuth.getInstance();

        //linking the view from the layout resource xml file to its activity class
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        //initiate a check box
        CheckBoxTerms = (CheckBox) findViewById(R.id.checkbox_terms);
        //used a real shopping's app terms and conditions
        String checkBoxText = "I agree to all the <a href='https://eur.shein.com/Terms-and-Conditions-a-399.html' > Terms and Conditions</a>";
        //initiate a button
        register = (Button) findViewById(R.id.register);

        CheckBoxTerms.setText(Html.fromHtml(checkBoxText));
        CheckBoxTerms.setMovementMethod(LinkMovementMethod.getInstance());

        CheckBoxTerms.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(CheckBoxTerms.isChecked()){
                    //if checked, register button can be tapped
                    register.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "You agreed with the terms and conditions", Toast.LENGTH_LONG).show();
                }
                else{
                    //else not checked,register button cannot be tapped
                    register.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Tick the terms and conditions to continue", Toast.LENGTH_LONG).show();
                }
            }
        });
        register.setEnabled(false);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            //invoking register method
            public void onClick(View v) {
                register(null);
            }
        });
    }

    public void register(View view) {
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        //user input validation methods
        if (TextUtils.isEmpty(userName)){
            Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_LONG).show();
            return;
        }
        else if  (TextUtils.isEmpty(userEmail)){
            Toast.makeText(getApplicationContext(), "Enter Email", Toast.LENGTH_LONG).show();
            return;
        }
        else if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();
            return;
        }
        else if (userPassword.length()< 8){
            Toast.makeText(getApplicationContext(), "Password too short, requires a minimum of 8 characters long", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            //creating a new account for a new user and store it in the firebase
            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registered Failed: " + task.getException(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }


}