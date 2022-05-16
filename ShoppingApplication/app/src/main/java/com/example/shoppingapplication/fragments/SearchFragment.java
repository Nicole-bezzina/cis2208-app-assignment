package com.example.shoppingapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoppingapplication.R;
import com.example.shoppingapplication.adapters.ShowAllAdapter;
import com.example.shoppingapplication.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    EditText searchBox;
    private List<ShowAllModel> showAllModelList;
    private RecyclerView recyclerViewSearch;
    private ShowAllAdapter showAllAdapter;

    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        db = FirebaseFirestore.getInstance();

        recyclerViewSearch = root.findViewById(R.id.search_rec);
        searchBox = root.findViewById(R.id.search_box);
        showAllModelList = new ArrayList<>();
        showAllAdapter = new ShowAllAdapter(getContext(), showAllModelList);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(showAllAdapter);
        recyclerViewSearch.setHasFixedSize(true);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()){
                    showAllModelList.clear();
                    showAllAdapter.notifyDataSetChanged();
                } else {
                    searchItem(editable.toString());
                }

            }

        });

        return root;
    }

    private void searchItem(String type) {
        if (!type.isEmpty()){
            db.collection("ShowAll").whereEqualTo("name", type).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null){
                                showAllModelList.clear();
                                showAllAdapter.notifyDataSetChanged();
                                for(DocumentSnapshot documentSnapshot : task.getResult().getDocuments()){
                                    ShowAllModel showAllModel = documentSnapshot.toObject(ShowAllModel.class);
                                    showAllModelList.add(showAllModel);
                                    showAllAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
        }

    }

}