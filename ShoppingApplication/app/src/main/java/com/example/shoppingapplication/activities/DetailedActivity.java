package com.example.shoppingapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shoppingapplication.R;
import com.example.shoppingapplication.models.NewItemsModel;
import com.example.shoppingapplication.models.SaleItemsModel;
import com.example.shoppingapplication.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating, name, description, price, quantity;
    Button addToCart, buyNow;
    ImageView addItems, removeItems;

    Toolbar toolbar;
    int totalQuantity = 1;
    int totalPrice = 0;

    //New Items
    NewItemsModel newItemsModel = null;

    //Sale Items
    SaleItemsModel saleItemsModel = null;

    //Show All
    ShowAllModel showAllModel = null;

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        //Initialising Firebase authentication and firestore
        firestore = FirebaseFirestore.getInstance();
        auth =  FirebaseAuth.getInstance();

        //invoking toolbar
        toolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final Object obj = getIntent().getSerializableExtra("detailed");

        if(obj instanceof NewItemsModel){
            newItemsModel = (NewItemsModel) obj;
        }
        else if (obj instanceof SaleItemsModel){
            saleItemsModel = (SaleItemsModel) obj;
        }
        else if (obj instanceof ShowAllModel){
            showAllModel = (ShowAllModel) obj;
        }

        //linking the view from the layout resource xml file to its activity class
        detailedImg = findViewById(R.id.detailed_img);
        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.detailed_name);
        description = findViewById(R.id.detailed_desc);
        rating = findViewById(R.id.rating);
        price = findViewById(R.id.detailed_price);

        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);
        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);

        //New Items
        if(newItemsModel != null){
            Glide.with(getApplicationContext()).load(newItemsModel.getImg_url()).into(detailedImg);
            name.setText(newItemsModel.getName());
            description.setText(newItemsModel.getDescription());
            rating.setText(newItemsModel.getRating());
            price.setText(String.valueOf(newItemsModel.getPrice()));

            totalPrice = newItemsModel.getPrice() * totalQuantity;
        }

        //Sale Items
        if(saleItemsModel != null){
            Glide.with(getApplicationContext()).load(saleItemsModel.getImg_url()).into(detailedImg);
            name.setText(saleItemsModel.getName());
            description.setText(saleItemsModel.getDescription());
            rating.setText(saleItemsModel.getRating());
            price.setText(String.valueOf(saleItemsModel.getPrice()));

            totalPrice = saleItemsModel.getPrice() * totalQuantity;
        }

        //Show all Items
        if(showAllModel != null){
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            description.setText(showAllModel.getDescription());
            rating.setText(showAllModel.getRating());
            price.setText(String.valueOf(showAllModel.getPrice()));

            totalPrice = showAllModel.getPrice() * totalQuantity;
        }

        //Buy Now button invoking address activity
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedActivity.this, AddressActivity.class);

                if (newItemsModel != null){
                    intent.putExtra("Item", newItemsModel);
                }
                if (saleItemsModel != null){
                    intent.putExtra("Item", saleItemsModel);
                }
                if (showAllModel != null){
                    intent.putExtra("Item", showAllModel);
                }


                startActivity(intent);
            }
        });

        //Add to Cart button invoking addToCart method
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });

        //plus icon image set as a button in order to increase quantity and price according to the item
        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity < 10){
                    totalQuantity ++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if(newItemsModel != null){
                        totalPrice = newItemsModel.getPrice() * totalQuantity;
                    }
                    else if (saleItemsModel != null){
                        totalPrice = saleItemsModel.getPrice() * totalQuantity;
                    }
                    else if (showAllModel != null){
                        totalPrice = showAllModel.getPrice() * totalQuantity;
                    }
                }

            }
        });

        //minus icon image set as a button in order to decrease quantity and price according to the item
        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity > 1){
                    totalQuantity --;
                    quantity.setText(String.valueOf(totalQuantity));
                }


            }
        });
    }


    private void addToCart() {
        String saveCurrentTime, saveCurrentDate;

        //using calender to store current date and time of when the items were put into cart
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        //cart items details
        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("productName",name.getText().toString());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        //items added to cart added to collection
        firestore.collection("AddToCart")
                .document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(DetailedActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}