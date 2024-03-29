package com.example.magazinarticolesportive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.magazinarticolesportive.models.Products;
import com.example.magazinarticolesportive.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addProductToCartBtn;
    private FloatingActionButton addProductToWishListBtn;
    private ImageView productImage;
    private ElegantNumberButton quantityBtn;
    private TextView productPrice, productDescription, productName, productSport, productSize, productCategory, productGender;
    private String  productID = "";
    private double priceDou;
    private String nameStr="", descriptionStr="", sportStr="", categoryStr="", sizeStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("pid");

        productImage = findViewById(R.id.product_image_details);
        quantityBtn = findViewById(R.id.quantity_details_btn);
        addProductToCartBtn = findViewById(R.id.add_to_cart_details_btn);
        productName = findViewById(R.id.product_name_details);
        productDescription = findViewById(R.id.product_description_details);
        productPrice = findViewById(R.id.product_price_details);
        productSport = findViewById(R.id.product_sport_details);
        productSize = findViewById(R.id.product_size_details);
        productCategory = findViewById(R.id.product_category_details);
        productGender = findViewById(R.id.product_gender_details);
        addProductToWishListBtn = findViewById(R.id.add_to_wish_details_btn);

        getProductDetails(productID);

        addProductToWishListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToWishList();
            }
        });

        addProductToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addToCartList();

            }
        });
    }

    private void addToWishList() {

        final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference().child("Wish List");

        final HashMap<String, Object> wishMap = new HashMap<>();
        wishMap.put("pid", productID);
        wishMap.put("name", nameStr);
        wishMap.put("price", priceDou);
        wishMap.put("category", categoryStr);
        wishMap.put("sport", sportStr);

        wishListRef.child(Prevalent.currentUser.getPhone())
                .child("Products").child(productID).updateChildren(wishMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProductDetailsActivity.this, "Product added to your wish list.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void addToCartList() {

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("name", nameStr);
        cartMap.put("price", priceDou);
        cartMap.put("quantity", Integer.parseInt(quantityBtn.getNumber()));

        cartListRef.child(Prevalent.currentUser.getPhone()).child("Products")
                .child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(ProductDetailsActivity.this, "Product added to your cart.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                });

    }

    private void getProductDetails(final String productID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);

                    checkQuantity(products.getQuantity());
                    saveDetails(products);

                    Picasso.get().load(products.getImage()).into(productImage);
                    productName.setText(nameStr);
                    productDescription.setText("Description: " + descriptionStr);
                    productPrice.setText("Price: " + priceDou + " RON");
                    productSport.setText("Sport: " + sportStr);
                    productCategory.setText("Category: " + categoryStr);
                    productSize.setText("Size: " + sizeStr);
                    productGender.setText("Gender: " + products.getGender());
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveDetails(Products products) {

        nameStr = products.getName();
        descriptionStr = products.getDescription();
        priceDou = products.getPrice();
        sportStr = products.getSport();
        categoryStr = products.getCategory();
        sizeStr = products.getSize();
    }

    private void checkQuantity(int quantity) {
        if(quantity <= 10 )
        {
            if(quantity > 0) {
                quantityBtn.setRange(1, quantity);
            }else {
                Toast.makeText(ProductDetailsActivity.this, "The product is sold out", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}
