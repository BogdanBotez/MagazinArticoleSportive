package com.example.magazinarticolesportive.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.magazinarticolesportive.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminEditProductActivity extends AppCompatActivity {

    private Button saveProductBtn, deleteProductBtn;
    private EditText name, price, quantity;
    private ImageView image;
    private String productID = "";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product);

        productID = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);

        saveProductBtn = findViewById(R.id.save_product_edit_btn);
        image = findViewById(R.id.product_image_edit);
        deleteProductBtn = findViewById(R.id.delete_product_edit_btn);
        name = findViewById(R.id.product_name_edit);
        price = findViewById(R.id.product_price_edit);
        quantity = findViewById(R.id.product_quantity_edit);

        displayProductInfo();

        saveProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }

    private void deleteProduct() {

        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(AdminEditProductActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminEditProductActivity.this, "The product have been deleted!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveChanges() {
        String sName = name.getText().toString();
        String sPrice = price.getText().toString();
        String sQuantity = quantity.getText().toString();
        String lastQuantity = quantity.getHint().toString();
        lastQuantity = lastQuantity.replaceAll("[^0-9]", "");

        int newQuantity = Integer.parseInt(lastQuantity) + Integer.parseInt(sQuantity);

        if (sName.equals("")) {
            Toast.makeText(this, "Enter the name for the product", Toast.LENGTH_SHORT).show();
        } else if (sPrice.equals("")) {
            Toast.makeText(this, "Enter the price for the product", Toast.LENGTH_SHORT).show();
        } else if (sQuantity.equals("")) {
            Toast.makeText(this, "Enter the quantity for the product", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productID);
            productMap.put("name", sName);
            productMap.put("price", Double.parseDouble(sPrice));
            productMap.put("quantity", newQuantity);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminEditProductActivity.this, "Changes saved successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminEditProductActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displayProductInfo() {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String sName = dataSnapshot.child("name").getValue().toString();
                    String sPrice = dataSnapshot.child("price").getValue().toString();
                    final String sQuantity = dataSnapshot.child("quantity").getValue().toString();
                    String sImage = dataSnapshot.child("image").getValue().toString();

                    name.setText(sName);
                    price.setText(sPrice);
                    quantity.setHint("Available quantity: " + sQuantity + "; enter how much you want to add");
                    quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(hasFocus){
                                quantity.setError("Available quantity: " + sQuantity +
                                        "; enter how much you want to add");
                            }
                        }
                    });

                    quantity.setText(sQuantity);
                    Picasso.get().load(sImage).into(image);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.getDetails();
            }
        });
    }
}
