package com.example.magazinarticolesportive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView womenTops, womenPants, womenShoes, womenAccessories,
            menTops, menPants, menShoes, menAccessories, sportEquipment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

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
