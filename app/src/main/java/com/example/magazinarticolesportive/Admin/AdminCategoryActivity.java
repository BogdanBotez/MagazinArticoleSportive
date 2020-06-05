package com.example.magazinarticolesportive.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.magazinarticolesportive.HomeActivity;
import com.example.magazinarticolesportive.MainActivity;
import com.example.magazinarticolesportive.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView womenTops, womenPants, womenShoes, womenAccessories,
            menTops, menPants, menShoes, menAccessories, sportEquipment;

    private Button logoutBtn, checkOrdersBtn, editProductBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        logoutBtn = findViewById(R.id.admin_logout_btn);
        checkOrdersBtn = findViewById(R.id.check_orders_btn);
        editProductBtn = findViewById(R.id.edit_product_btn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminOrdersActivity.class);

                startActivity(intent);

            }
        });

        editProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, HomeActivity.class);
                intent.putExtra("admin", "admin");
                startActivity(intent);
            }
        });

        womenTops = findViewById(R.id.tops_women);
        womenPants = findViewById(R.id.pants_women);
        womenShoes = findViewById(R.id.shoes_women);
        womenAccessories = findViewById(R.id.accessories_women);
        menTops = findViewById(R.id.tops_men);
        menPants = findViewById(R.id.pants_men);
        menShoes = findViewById(R.id.shoes_men);
        menAccessories = findViewById(R.id.accessories_men);
        sportEquipment = findViewById(R.id.sport_equipment);


        womenTops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "womenTops");
                startActivity(intent);
            }
        });

        womenPants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "womenPants");
                startActivity(intent);
            }
        });

        womenAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "womenAccessories");
                startActivity(intent);
            }
        });

        womenShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "womenShoes");
                startActivity(intent);
            }
        });

        menTops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "menTops");
                startActivity(intent);
            }
        });

        menPants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "menPants");
                startActivity(intent);
            }
        });

        menAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "menAccessories");
                startActivity(intent);
            }
        });

        menShoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "menShoes");
                startActivity(intent);
            }
        });

        sportEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "sportEquipment");
                startActivity(intent);
            }
        });

    }
}
