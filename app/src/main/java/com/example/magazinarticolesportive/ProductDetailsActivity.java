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
import com.example.magazinarticolesportive.Model.Products;
import com.example.magazinarticolesportive.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addProductToCartBtn;
    private ImageView productImage;
    private ElegantNumberButton quantityBtn;
    private TextView productPrice, productDescription, productName, productSport, productSize, productCategory;
    private String productID = "";
    private String state = "";

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
        //ToDo the rest

        getProductDetails(productID);

        addProductToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(state.equals("Order placed") || state.equals("Order shipped")){
//                    Toast.makeText(ProductDetailsActivity.this, "You will have the option to add products to the cart once you will have the last order finished", Toast.LENGTH_LONG).show();
//                }else{
                    addToCartList();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
    }

    private void addToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("DD MM YYYY");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("name", productName.getText().toString());
        cartMap.put("price", String.valueOf(productPrice.getText()));
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", quantityBtn.getNumber());
        cartMap.put("discount", "");

        //adaugarea in lista a produselor
        cartListRef.child("User View").child(Prevalent.currentUser.getPhone()).child("Products")
                .child(productID).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            cartListRef.child("Admin View").child(Prevalent.currentUser.getPhone()).child("Products")
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
                    }
                });


    }

    private void updateQuantity(final String qtyOrdered) {
        final DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Products prod = dataSnapshot.getValue(Products.class);

                int newQuantity = prod.getQuantity() - Integer.parseInt(qtyOrdered);
                HashMap<String, Object> updatedProduct = new HashMap<>();
                updatedProduct.put("quantity", newQuantity);
                productRef.updateChildren(updatedProduct);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getProductDetails(final String productID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);

                    if(products.getQuantity() <= 10 )
                    {
                        if(products.getQuantity() >= 0) {
                            quantityBtn.setRange(1, products.getQuantity());
                            //TODO testeaza daca functioneaza cand sunt 0 buc
                        }else {
                            Toast.makeText(ProductDetailsActivity.this, "The product is sold out", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    Picasso.get().load(products.getImage()).into(productImage);
                    productName.setText(products.getName());
                    productDescription.setText(products.getDescription());
                    productPrice.setText(String.valueOf(products.getPrice()));
                    productSport.setText(products.getSport());
                    productCategory.setText(products.getCategory());
                    productSize.setText(products.getSize());
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckOrderState(){
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    if(shippingState.equals("shipped")){
                        state = "Order completed";
                    }else if(shippingState.equals("not shipping")){

                        state = "Order placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
