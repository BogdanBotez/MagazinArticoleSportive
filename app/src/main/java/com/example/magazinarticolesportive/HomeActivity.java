package com.example.magazinarticolesportive;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magazinarticolesportive.admin.AdminEditProductActivity;
import com.example.magazinarticolesportive.models.Products;
import com.example.magazinarticolesportive.prevalent.Prevalent;
import com.example.magazinarticolesportive.User.CartActivity;
import com.example.magazinarticolesportive.User.WishListActivity;
import com.example.magazinarticolesportive.viewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference productReference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private String type = "";
    private String category = "all";
    private String searchCategory = "";
    private FirebaseRecyclerOptions<Products> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().get("admin").toString();
        }

        productReference = FirebaseDatabase.getInstance().getReference().child("Products");

        Paper.init(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type.equals("admin")) {
                    Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                    startActivity(intent);
                }
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        if (!type.equals("admin")) {
            userNameTextView.setText(Prevalent.currentUser.getName());
            Picasso.get().load(Prevalent.currentUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);
        }
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (category.equals("all")) {

            options = new FirebaseRecyclerOptions.Builder<Products>()
                    .setQuery(productReference, Products.class)
                    .build();
        } else {
            options =
                    new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(productReference.orderByChild("category").startAt(category).endAt(category), Products.class)
                            .build();
        }


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getName());
                        holder.txtProductDescription.setText("Description: " + model.getDescription());
                        holder.txtProductPrice.setText("Price: " + model.getPrice() + " RON");
                        holder.txtSize.setText("Size: " + model.getSize());
                        Picasso.get().load(model.getImage()).into(holder.productImageView);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (type.equals("admin")) {
                                    Intent intent = new Intent(HomeActivity.this, AdminEditProductActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);

                                } else {
                                    Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
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

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public void OnBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings)
//        {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.nav_cart) {
            if (!type.equals("admin")) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_search) {

            getSearchCategory();


        } else if (id == R.id.nav_categories) {
            applyCategory();

        } else if (id == R.id.nav_settings) {
            if (!type.equals("admin")) {
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_orders) {
            if (!type.equals("admin")) {
                Intent intent = new Intent(HomeActivity.this, UserOrdersActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_wish) {
            if (!type.equals("admin")) {
                Intent intent = new Intent(HomeActivity.this, WishListActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_coupons) {
            if (!type.equals("admin")) {
                Intent intent = new Intent(HomeActivity.this, CouponsActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_logout) {
            Paper.book().destroy();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getSearchCategory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Choose the category you want to search by.");
        final MaterialSpinner categorySpinner = new MaterialSpinner(HomeActivity.this);
        builder.setView(categorySpinner);
        categorySpinner.setDropdownHeight(1400);
        categorySpinner.setItems("", "name", "sport", "size", "gender");
        categorySpinner.setTextColor(Color.BLACK);
        categorySpinner.setArrowColor(Color.BLACK);

        categorySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                searchCategory = item.toString();
            }
        });


        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(HomeActivity.this, SearchProductsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (type.equals("admin")) {
                    intent.putExtra("type", "admin");
                } else
                    intent.putExtra("type", "user");

                intent.putExtra("category", searchCategory);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void applyCategory() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Choose the category you want.");
        final MaterialSpinner categorySpinner = new MaterialSpinner(HomeActivity.this);
        builder.setView(categorySpinner);
        categorySpinner.setItems("", "all", "tops", "pants", "shoes", "equipment");
        categorySpinner.setTextColor(Color.BLACK);
        categorySpinner.setArrowColor(Color.BLACK);

        categorySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                category = item.toString();
            }
        });

        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(HomeActivity.this, "This is the category: " + category, Toast.LENGTH_SHORT).show();
                onStart();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
