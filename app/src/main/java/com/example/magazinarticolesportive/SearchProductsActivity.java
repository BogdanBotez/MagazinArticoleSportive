package com.example.magazinarticolesportive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.magazinarticolesportive.admin.AdminEditProductActivity;
import com.example.magazinarticolesportive.models.Products;
import com.example.magazinarticolesportive.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

public class SearchProductsActivity extends AppCompatActivity {


    private Button searchBtn;
    private EditText inputText;
    private RecyclerView searchList;
    private String search;
    private String type="", category="name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        inputText = findViewById(R.id.search_product_name);
        searchBtn = findViewById(R.id.search_product_btn);
        searchList = findViewById(R.id.search_list);
        searchList.setLayoutManager(new LinearLayoutManager(SearchProductsActivity.this));
        type = getIntent().getStringExtra("type");
        category = getIntent().getStringExtra("category");

        inputText.setHint(inputText.getHint() + setHintByCategory());

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = inputText.getText().toString();
                search = StringUtils.lowerCase(search);
                search = StringUtils.capitalize(search);
                onStart();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ref.orderByChild(category).startAt(search).endAt(search + "\uf8ff"), Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter <Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                holder.txtProductName.setText(model.getName());
                holder.txtProductDescription.setText(model.getDescription());
                holder.txtProductPrice.setText("Price = " + model.getPrice() +"RON");
                Picasso.get().load(model.getImage()).into(holder.productImageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(type.equals("admin")) {
                            Intent intent = new Intent(SearchProductsActivity.this, AdminEditProductActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);
                        } else{
                            Intent intent = new Intent(SearchProductsActivity.this, ProductDetailsActivity.class);
                            intent.putExtra("pid", model.getPid());
                            startActivity(intent);
                        }
                    }
                });
            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(v);
                return holder;
            }
        };

        searchList.setAdapter(adapter);
        adapter.startListening();
    }

    private String setHintByCategory(){
        String text = "";
        switch (category) {
            case "name":
                text = " name";
                break;
            case "sport":
                text = " sport.\nE.g.: Football";
                break;
            case "gender":
                text = " gender.\nE.g.: Male, Unisex";
                break;
            case "size":
                text = " size.\nE.g.: S, 42";
                break;
        }
        return text;
    }
}