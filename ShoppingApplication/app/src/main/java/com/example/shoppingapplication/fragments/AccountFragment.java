package com.example.shoppingapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shoppingapplication.R;
import com.example.shoppingapplication.activities.HelpActivity;
import com.example.shoppingapplication.activities.OrdersActivity;
import com.example.shoppingapplication.activities.SettingsActivity;
import com.example.shoppingapplication.activities.VouchersActivity;

public class AccountFragment extends Fragment {
    private Button settings;
    private Button vouchers;
    private Button orders;
    private Button help;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        settings = root.findViewById(R.id.settings);
        vouchers = root.findViewById(R.id.vouchers);
        orders = root.findViewById(R.id.orders);
        help = root.findViewById(R.id.help);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        vouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VouchersActivity.class);
                startActivity(intent);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrdersActivity.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HelpActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}