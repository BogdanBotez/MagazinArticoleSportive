package com.example.magazinarticolesportive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.magazinarticolesportive.models.Coupons;
import com.example.magazinarticolesportive.prevalent.Prevalent;
import com.example.magazinarticolesportive.viewHolder.CouponsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CouponsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        recyclerView = findViewById(R.id.coupons_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference couponsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentUser.getPhone()).child("Coupons");

        FirebaseRecyclerOptions<Coupons> options =
                new FirebaseRecyclerOptions.Builder<Coupons>()
                        .setQuery(couponsRef, Coupons.class).build();

        FirebaseRecyclerAdapter<Coupons, CouponsViewHolder> adapter = new FirebaseRecyclerAdapter<Coupons, CouponsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CouponsViewHolder holder, int position, @NonNull Coupons model) {
                holder.couponName.setText(model.getName());
                holder.couponValue.setText(model.getValue());
            }

            @NonNull
            @Override
            public CouponsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CouponsViewHolder holder = new CouponsViewHolder(v);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        {


        }
    }
}
