//TODO chestia cu comenzile multiple
package com.example.magazinarticolesportive.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magazinarticolesportive.HomeActivity;
import com.example.magazinarticolesportive.Model.Cart;
import com.example.magazinarticolesportive.Model.Products;
import com.example.magazinarticolesportive.Prevalent.Prevalent;
import com.example.magazinarticolesportive.ProductDetailsActivity;
import com.example.magazinarticolesportive.R;
import com.example.magazinarticolesportive.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView txtTotalPrice;
    private double totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessBtn = findViewById(R.id.next_process_btn);
        txtTotalPrice = findViewById(R.id.total_price);

        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalPrice < 250) {
                    Toast.makeText(CartActivity.this, "Orders below 250RON have a 25RON delivery fee.", Toast.LENGTH_SHORT).show();
                    totalPrice += 25;
                }

                Intent intent = new Intent(CartActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(totalPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        CheckProductQuantity();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentUser.getPhone())
                                .child("Products"), Cart.class).build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtQuantity.setText("Quantity =" + model.getQuantity());
                holder.txtName.setText(model.getName());
                holder.txtPrice.setText("Price = " + model.getPrice());
                double productPrice = Double.valueOf(model.getPrice()) * Double.valueOf(model.getQuantity());
                holder.txtTotalProductPrice.setText("Total price = " + productPrice + " RON");
                totalPrice += productPrice;
                txtTotalPrice.setText("Total price = " + totalPrice);

                //Cand se apasa pe un produs
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "Edit",
                                "Delete"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int option) {
                                //Edit
                                if (option == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                } else if (option == 1) {
                                    cartListRef.child("User View").child(Prevalent.currentUser.getPhone()).child("Products")
                                            .child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(CartActivity.this, "The product has been removed from the cart", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                            }

                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(v);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckProductQuantity() {

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevalent.currentUser.getPhone()).child("Products");
        final DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        cartListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotCart) {

                for (DataSnapshot cartSnapshot : dataSnapshotCart.getChildren()) {
                    final Products prod = cartSnapshot.getValue(Products.class);

                    productsRef.child(prod.getPid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshotProd) {

                            int prodStock = dataSnapshotProd.child("quantity").getValue(Integer.class);
                            if (prod.getQuantity() > prodStock) {
                                cartListRef.child(prod.getPid()).removeValue();
                                Toast.makeText(CartActivity.this, "The stock quantity for one or more products has changed.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                //Todo citeste putin despre flags ca poate intreaba
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
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
}
