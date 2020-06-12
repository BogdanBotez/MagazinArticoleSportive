package com.example.magazinarticolesportive.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magazinarticolesportive.HomeActivity;
import com.example.magazinarticolesportive.Model.Products;
import com.example.magazinarticolesportive.Prevalent.Prevalent;
import com.example.magazinarticolesportive.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {


    //Todo cost transport
    private EditText nameEditText, phoneEditText, addressEditText;
    private TextView totalPriceTxt;
    private Button confirmOrderBtn;
    private String totalPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        totalPrice = getIntent().getStringExtra("Total Price");
        confirmOrderBtn = findViewById(R.id.confirm_order_btn);
        nameEditText = findViewById(R.id.name_confirm_order);
        phoneEditText = findViewById(R.id.phone_confirm_order);
        addressEditText = findViewById(R.id.address_confirm_order);
        totalPriceTxt = findViewById(R.id.total_price_confirm_order);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
                updateQuantity();
            }
        });

    }

    private void Check() {
        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(addressEditText.getText().toString())) {
            Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
        } else {
            ConfirmOrder();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneEditText.setText(Prevalent.currentUser.getPhone());
        nameEditText.setText(Prevalent.currentUser.getName());
        addressEditText.setText(Prevalent.currentUser.getAddress());
        totalPriceTxt.setText("Total price:  " + totalPrice + "RON");
    }

    private void ConfirmOrder() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("DD MM YYYY");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS");
        saveCurrentTime = currentTime.format(calendar.getTime());

        String orderID = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentUser.getPhone()).push().getKey();

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentUser.getPhone()).child(orderID);
        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalPrice", totalPrice);
        ordersMap.put("name", nameEditText.getText().toString());
        ordersMap.put("phone", phoneEditText.getText().toString());
        ordersMap.put("address", addressEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    moveProductsToOrder(
                            FirebaseDatabase.getInstance().getReference().child("Cart List")
                                    .child("User View")
                                    .child(Prevalent.currentUser.getPhone()).child("Products"),
                            ordersRef.child("Products")
                    );
                    Toast.makeText(ConfirmOrderActivity.this, "You have confirmed the order!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmOrderActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }

    private void updateQuantity() {
        final DatabaseReference productsRef =
                FirebaseDatabase.getInstance().getReference()
                        .child("Products");

        final DatabaseReference cartListRef =
                FirebaseDatabase.getInstance().getReference()
                        .child("Cart List")
                        .child("User View")
                        .child(Prevalent.currentUser.getPhone())
                        .child("Products");

        cartListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshotCart) {

                for (DataSnapshot cartSnapshot : dataSnapshotCart.getChildren()) {
                    final Products cartProduct = cartSnapshot.getValue(Products.class);

                    productsRef.child(cartProduct.getPid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshotProduct) {
                            int quantity = dataSnapshotProduct.child("quantity").getValue(Integer.class);

                            HashMap<String, Object> updatedProduct = new HashMap<>();

                            quantity -= cartProduct.getQuantity();
                            updatedProduct.put("quantity", quantity);
                            productsRef.child(cartProduct.getPid()).updateChildren(updatedProduct);
                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void moveProductsToOrder(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError != null) {

                        } else {
                            fromPath.removeValue();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }
}