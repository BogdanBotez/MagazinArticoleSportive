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
import com.example.magazinarticolesportive.models.Coupons;
import com.example.magazinarticolesportive.models.Products;
import com.example.magazinarticolesportive.prevalent.Prevalent;
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

    private EditText nameEditText, phoneEditText, addressEditText, couponEditText;
    private TextView totalPriceTxt;
    private Button confirmOrderBtn, applyCouponBtn;
    private String totalPrice = "";
    private String totalPriceDisc = "";
    private String couponId = "";
    private boolean isDiscountable = true;
    private DatabaseReference couponsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentUser.getPhone()).child("Coupons");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        totalPrice = getIntent().getStringExtra("Total Price");
        confirmOrderBtn = findViewById(R.id.confirm_order_btn);
        applyCouponBtn = findViewById(R.id.apply_coupon_btn);
        nameEditText = findViewById(R.id.name_confirm_order);
        phoneEditText = findViewById(R.id.phone_confirm_order);
        addressEditText = findViewById(R.id.address_confirm_order);
        couponEditText = findViewById(R.id.coupon_confirm_order);
        totalPriceTxt = findViewById(R.id.total_price_confirm_order);


        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
                updateQuantity();
            }
        });

        applyCouponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDiscountable) {
                    applyCoupon();
                }else{
                    removeCoupon();
                }
            }
        });

    }

    private void removeCoupon() {
        totalPriceTxt.setText("Total Price: " + totalPrice + " RON");
        isDiscountable = true;
        applyCouponBtn.setText("apply coupon");
        Toast.makeText(this, "Coupon removed successfully.", Toast.LENGTH_SHORT).show();
    }

    private void applyCoupon() {

        couponsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot couponsListSnapshot : dataSnapshot.getChildren()) {
                    Coupons couponSnapshot = couponsListSnapshot.getValue(Coupons.class);
                    if (couponSnapshot.getName().equals(couponEditText.getText().toString().toUpperCase())) {
                        couponId = couponsListSnapshot.getKey();
                        double discount = couponSnapshot.getValue();
                        double totalPriceDiscount = Double.parseDouble(totalPrice) * ((100 - discount) / 100);
                        totalPriceDisc = String.valueOf(totalPriceDiscount);
                        totalPriceTxt.setText("Total Price: " + totalPriceDiscount + " RON");
                        isDiscountable = false;
                        applyCouponBtn.setText("remove coupon");
                        break;
                    }

                }
                if(isDiscountable) {
                    couponEditText.setError("The coupon doesn't exist");
                }
                else{
                    Toast.makeText(ConfirmOrderActivity.this, "Coupon applied successfully.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void check() {
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
        totalPriceTxt.setText("Total price: " + totalPrice + "RON");
    }

    private void ConfirmOrder() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        String orderID = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentUser.getPhone()).push().getKey();

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentUser.getPhone()).child(orderID);
        HashMap<String, Object> ordersMap = new HashMap<>();
        if(!isDiscountable) {
            ordersMap.put("totalPrice", totalPriceDisc);
        } else {
            ordersMap.put("totalPrice", totalPrice);
        }
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
                                    .child(Prevalent.currentUser.getPhone()).child("Products"),
                            ordersRef.child("Products")
                    );
                    if(!isDiscountable) {
                        deleteCoupon(couponId);
                    }else {
                        addCoupon(Double.parseDouble(totalPrice));
                    }
                    Toast.makeText(ConfirmOrderActivity.this, "You have confirmed the order!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmOrderActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }

    private void addCoupon(double price) {
        HashMap<String, Object> couponMap = new HashMap<>();
        if(price >= 2000){
            couponMap.put("name", "EXTRA20");
            couponMap.put("value", 20);
        }else if(price >= 1500){
            couponMap.put("name", "EXTRA15");
            couponMap.put("value", 15);
        }else if(price >= 1000){
            couponMap.put("name", "EXTRA10");
            couponMap.put("value", 10);
        }
        couponsRef.push().updateChildren(couponMap);

    }

    private void deleteCoupon(String couponId) {
        couponsRef.child(couponId).removeValue();
    }

    private void updateQuantity() {
        final DatabaseReference productsRef =
                FirebaseDatabase.getInstance().getReference()
                        .child("Products");

        final DatabaseReference cartListRef =
                FirebaseDatabase.getInstance().getReference()
                        .child("Cart List")
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