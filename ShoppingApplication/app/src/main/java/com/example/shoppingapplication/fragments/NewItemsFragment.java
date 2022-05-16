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
import com.example.shoppingapplication.adapters.NewItemsAdapter;
import com.example.shoppingapplication.models.NewItemsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NewItemsFragment extends Fragment {

    RecyclerView newItemsRecyclerView;
    NewItemsAdapter newItemsAdapter;
    List<NewItemsModel> newItemsModelList;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_new_items, container, false);

        db = FirebaseFirestore.getInstance();

        newItemsRecyclerView = root.findViewById(R.id.new_item_rec_frag);
        newItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL,false));
        newItemsModelList = new ArrayList<>();
        newItemsAdapter = new NewItemsAdapter(getActivity(), newItemsModelList);
        newItemsRecyclerView.setAdapter(newItemsAdapter);
        db.collection("NewItems")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NewItemsModel newItemsModel = document.toObject(NewItemsModel.class);
                                newItemsModelList.add(newItemsModel);
                                newItemsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getActivity(),""+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return root;
    }
}