package com.example.shoppingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shoppingapplication.R;
import com.example.shoppingapplication.databinding.ActivityMainBinding;
import com.example.shoppingapplication.fragments.AccountFragment;
import com.example.shoppingapplication.fragments.HomeFragment;
import com.example.shoppingapplication.fragments.NewItemsFragment;
import com.example.shoppingapplication.fragments.SaleItemsFragment;
import com.example.shoppingapplication.fragments.SearchFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Fragment homeFragment, searchFragment;
    FirebaseAuth auth;
    Toolbar toolbar;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        auth = FirebaseAuth.getInstance();

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.newItems:
                    replaceFragment(new NewItemsFragment());
                    break;
                case R.id.saleItems:
                    replaceFragment(new SaleItemsFragment());
                    break;
                case R.id.account:
                    replaceFragment(new AccountFragment());
                    break;
            }
            return true;
        });

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        homeFragment = new HomeFragment();
        replaceFragment(homeFragment);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_container, fragment);
        fragmentTransaction.commit();
    }

    private void loadSearchFragment(Fragment searchFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container, searchFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_search){
            searchFragment = new SearchFragment();
            loadSearchFragment(searchFragment);
        }
        if (id == R.id.menu_logout){
            auth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
        if (id == R.id.menu_my_cart){
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        }
        return true;
    }
}