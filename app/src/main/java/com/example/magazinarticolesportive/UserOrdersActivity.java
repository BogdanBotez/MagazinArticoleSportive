package com.example.magazinarticolesportive;

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

import com.example.magazinarticolesportive.Model.Orders;
import com.example.magazinarticolesportive.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserOrdersActivity extends AppCompatActivity {


    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    //TODO scade quantity din baza de date products.
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentUser.getPhone());
        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        //setez un query cu ajut RecyclerOptions
        FirebaseRecyclerOptions<Orders> options = new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(ordersRef, Orders.class).build();
        FirebaseRecyclerAdapter<Orders, OrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<Orders, OrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrdersViewHolder holder, final int position, @NonNull final Orders model) {
                        holder.name.setText("Name: " + model.getName());
                        holder.orderPhone.setText("Phone: " + model.getPhone());
                        holder.orderPrice.setText("Price: " + model.getTotalPrice());
                        holder.orderAddress.setText("Address: " + model.getAddress());
                        holder.date.setText("Date: " + model.getDate());
                        holder.time.setText("Time: " + model.getTime());

                        holder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String ID = getRef(position).getKey();

                                Intent intent = new Intent(UserOrdersActivity.this, OrderDetailsActivity.class);
                                intent.putExtra("id", ID);
                                startActivity(intent);
                            }
                        });

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[]{
                                        "Delete",
                                        "Cancel"
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserOrdersActivity.this);
                                builder.setTitle("Have you shipped this order?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0){
                                            String ID = getRef(position).getKey();
                                            RemoveOrder(ID);


                                        }else{
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_orders_layout, parent,false);
                        return new OrdersViewHolder(v);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoveOrder(String id) {
        ordersRef.child(id).removeValue();
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder{
        public TextView orderPhone, orderPrice, orderAddress, name, date, time;
        public Button showOrdersBtn;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name_order);
            orderPrice = itemView.findViewById(R.id.total_price_order);
            orderAddress = itemView.findViewById(R.id.address_order);
            orderPhone = itemView.findViewById(R.id.phone_number_order);
            showOrdersBtn = itemView.findViewById(R.id.show_products_order_btn);
            time = itemView.findViewById(R.id.time_order);
            date = itemView.findViewById(R.id.date_order);

        }
    }
}

