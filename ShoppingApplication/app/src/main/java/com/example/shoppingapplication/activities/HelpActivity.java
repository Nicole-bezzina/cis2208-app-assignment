package com.example.shoppingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.Objects;

public class HelpActivity extends AppCompatActivity {
    Toolbar toolbar;
    private EditText feedback;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        submit = findViewById(R.id.submit);

        //invoking toolbar
        toolbar = findViewById(R.id.help_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //linking the view from the layout resource xml file to its activity class
        TextView linkTextView1 = findViewById(R.id.faq1);
        linkTextView1.setMovementMethod(LinkMovementMethod.getInstance());
        TextView linkTextView2 = findViewById(R.id.faq2);
        linkTextView2.setMovementMethod(LinkMovementMethod.getInstance());
        TextView linkTextView3 = findViewById(R.id.faq3);
        linkTextView3.setMovementMethod(LinkMovementMethod.getInstance());

        //submit button invoking submit method
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                submit(null);
            }
        });
    }

    public void submit(View view) {
        feedback = findViewById(R.id.contactUs);

        //storing user feedback in firebase realtime database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object value = snapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HelpActivity.this,"Failed to read value", Toast.LENGTH_SHORT).show();
            }
        });

        ref.child("Users").child("Feedback").setValue(feedback.getText().toString());
        Toast.makeText(HelpActivity.this, "Thanks for your feedback!", Toast.LENGTH_SHORT).show();

    }
}