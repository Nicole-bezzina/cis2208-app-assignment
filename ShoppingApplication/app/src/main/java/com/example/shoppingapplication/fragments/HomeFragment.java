package com.example.shoppingapplication.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.shoppingapplication.R;
import com.example.shoppingapplication.activities.ShowAllActivity;
import com.example.shoppingapplication.adapters.CategoryAdapter;
import com.example.shoppingapplication.adapters.NewItemsAdapter;
import com.example.shoppingapplication.adapters.SaleItemsAdapter;
import com.example.shoppingapplication.models.CategoryModel;
import com.example.shoppingapplication.models.NewItemsModel;
import com.example.shoppingapplication.models.SaleItemsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView catShowAll, newItemsShowAll, saleItemsShowAll;

    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    RecyclerView catRecyclerView, newItemsRecyclerView, saleRecyclerView;

    //Category RecyclerView
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    //New Item RecyclerView
    NewItemsAdapter newItemsAdapter;
    List<NewItemsModel> newItemsModelList;

    //Sale Item RecyclerView
    SaleItemsAdapter saleItemsAdapter;
    List<SaleItemsModel> saleItemsModelList;

    //FireStore
    FirebaseFirestore db;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();

        //linking the view from the layout resource xml file to its activity class
        progressDialog = new ProgressDialog(getActivity());
        catRecyclerView = root.findViewById(R.id.rec_category);
        newItemsRecyclerView = root.findViewById(R.id.new_item_rec);
        saleRecyclerView = root.findViewById(R.id.sale_rec);

        catShowAll = root.findViewById(R.id.category_see_all);
        newItemsShowAll = root.findViewById(R.id.new_items_see_all);
        saleItemsShowAll = root.findViewById(R.id.sale_see_all);

        //the 3 see all options found in the home screen set to invoke the show all activity class

        catShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });

        newItemsShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });

        saleItemsShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ShowAllActivity.class);
                startActivity(intent);
            }
        });

        linearLayout = root.findViewById(R.id.home_layout);
        linearLayout.setVisibility(View.GONE);

        //adding onto the image slider
        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.banner1,"Discount on dresses", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner2,"Discount on accessories", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.banner3,"70% off", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);

        progressDialog.setTitle("Welcome to The Wardrobe App");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Category section - displaying items found in the firebase AllCategories collection
        catRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(), categoryModelList);
        catRecyclerView.setAdapter(categoryAdapter);
        db.collection("AllCategories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                                linearLayout.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        //New Items section - displaying items found in the firebase New Items collection
        newItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
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
                            Toast.  makeText(getActivity(),""+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Sale Items section - displaying items found in the firebase SaleItems collection
        saleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
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
                            Toast.makeText(getActivity(),""+task.getException(),Toast.LENGTH_LONG).show();
                        }
                    }
                });


        return root;
    }
}
