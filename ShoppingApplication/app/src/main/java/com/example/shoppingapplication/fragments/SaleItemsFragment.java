package com.example.shoppingapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shoppingapplication.R;
import com.example.shoppingapplication.adapters.SaleItemsAdapter;
import com.example.shoppingapplication.models.SaleItemsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SaleItemsFragment extends Fragment {

    SaleItemsAdapter saleItemsAdapter;
    List<SaleItemsModel> saleItemsModelList;
    RecyclerView saleRecyclerView;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sale_items, container, false);

        db = FirebaseFirestore.getInstance();

        //displaying items found in the firebase Sale Items collection
        saleRecyclerView = root.findViewById(R.id.sale_rec_frag);
        saleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
        saleItemsModelList = new ArrayList<>();
        saleItemsAdapter = new SaleItemsAdapter(getActivity(), saleItemsModelList);
        saleRecyclerView.setAdapter(saleItemsAdapter);
        db.collection("SaleItems")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SaleItemsModel saleItemsModel = document.toObject(SaleItemsModel.class);
                                saleItemsModelList.add(saleItemsModel);
                                saleItemsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(),""+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return root;
    }
}