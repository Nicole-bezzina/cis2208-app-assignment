package com.example.shoppingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.shoppingapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddAddressActivity extends AppCompatActivity {
    EditText name, address, city, postalCode, phoneNumber;
    Toolbar toolbar;
    Button addAddressButton;

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        //invoking toolbar
        toolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Initialising Firebase authentication and firestore
        auth =  FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        //linking the view from the layout resource xml file to its activity class
        name = findViewById(R.id.ad_name);
        address = findViewById(R.id.ad_address);
        city = findViewById(R.id.ad_city);
        postalCode = findViewById(R.id.ad_code);
        phoneNumber = findViewById(R.id.ad_phone);
        addAddressButton = findViewById(R.id.ad_add_address);

        //adding the address inputted by the user to the firebase collection CurrentUser
        addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                String userCity = city.getText().toString();
                String userAddress = address.getText().toString();
                String userCode = postalCode.getText().toString();
                String userNumber = phoneNumber.getText().toString();

                String final_address = "";

                if (!userName.isEmpty()){
                    final_address += userName + ", ";
                }
                if (!userCity.isEmpty()){
                    final_address += userCity + ", ";
                }
                if (!userAddress.isEmpty()){
                    final_address += userAddress + ", ";
                }
                if (!userCode.isEmpty()){
                    final_address += userCode + ", ";
                }
                if (!userNumber.isEmpty()){
                    final_address += userNumber + ", ";
                }
                //making sure all details were filled in
                if (!userName.isEmpty() && !userCity.isEmpty() && !userAddress.isEmpty() && !userCode.isEmpty() && !userNumber.isEmpty()) {
                    Map<String,String> map = new HashMap<>();
                    map.put("userAddress", final_address);

                    firestore.collection("CurrentUser")
                            .document(auth.getCurrentUser().getUid())
                            .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AddAddressActivity.this,"Address Added",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(AddAddressActivity.this, DetailedActivity.class));
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(AddAddressActivity.this,"Kindly fill in all details to proceed",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}