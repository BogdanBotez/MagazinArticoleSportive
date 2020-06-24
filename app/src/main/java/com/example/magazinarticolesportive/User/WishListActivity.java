package com.example.magazinarticolesportive.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magazinarticolesportive.HomeActivity;
import com.example.magazinarticolesportive.models.Products;
import com.example.magazinarticolesportive.prevalent.Prevalent;
import com.example.magazinarticolesportive.ProductDetailsActivity;
import com.example.magazinarticolesportive.R;
import com.example.magazinarticolesportive.viewHolder.WishViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WishListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button homeBtn;
    private TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        recyclerView = findViewById(R.id.wish_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        homeBtn = findViewById(R.id.home_wish_btn);
        txtMessage = findViewById(R.id.txt_msg_wish);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WishListActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        txtMessage.findViewById(R.id.txt_msg_wish);

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference().child("Wish List");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(wishListRef
                                .child(Prevalent.currentUser.getPhone())
                                .child("Products"), Products.class).build();
        FirebaseRecyclerAdapter<Products, WishViewHolder> adapter = new FirebaseRecyclerAdapter<Products, WishViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull WishViewHolder holder, int position, final Products model) {
                holder.txtCategory.setText("Category: " + model.getCategory());
                holder.txtName.setText(model.getName());
                holder.txtPrice.setText("Price = " + model.getPrice());
                holder.txtSport.setText("Sport: " + model.getSport());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "View",
                                "Delete"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(WishListActivity.this);
                        builder.setTitle("Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int option) {
                                if (option == 0) {
                                    Intent intent = new Intent(WishListActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                } else if (option == 1) {
                                    wishListRef.child(Prevalent.currentUser.getPhone()).child("Products")
                                            .child(model.getPid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(WishListActivity.this, "The product has been removed from the wish list", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(WishListActivity.this, HomeActivity.class);
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
            public WishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_items_layout, parent, false);
                WishViewHolder holder = new WishViewHolder(v);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

}
