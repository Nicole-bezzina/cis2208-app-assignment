package com.example.shoppingapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.shoppingapplication.R;
import com.example.shoppingapplication.databinding.ActivityMainBinding;
import com.example.shoppingapplication.fragments.AccountFragment;
import com.example.shoppingapplication.fragments.HomeFragment;
import com.example.shoppingapplication.fragments.NewItemsFragment;
import com.example.shoppingapplication.fragments.SaleItemsFragment;

import java.util.Objects;

public class VouchersActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers);
        //invoking toolbar
        toolbar = findViewById(R.id.vouchers_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}