package com.example.magazinarticolesportive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AdminAddProductActivity extends AppCompatActivity {

    private String Category;
    private Button AddProductButton;
    private EditText InputProductName, InputProductDescription, InputProductPrice,
        InputProductSize;
    private ImageView InputProductImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        //Primeste valoarea dupa cheia "category" , ex: womenTops
        Category = getIntent().getExtras().get("category").toString();

        AddProductButton = findViewById(R.id.add_product_btn);
        InputProductName = findViewById(R.id.product_name);
        InputProductDescription = findViewById(R.id.product_description);
        InputProductPrice = findViewById(R.id.product_price);
        InputProductSize = findViewById(R.id.product_size);
        InputProductImage = findViewById(R.id.input_product_image);

    }
}
