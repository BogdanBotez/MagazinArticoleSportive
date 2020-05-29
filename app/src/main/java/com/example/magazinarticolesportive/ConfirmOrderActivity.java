package com.example.magazinarticolesportive;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfirmOrderActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, cityEditText;
    private Button confirmOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        confirmOrderBtn = findViewById(R.id.confirm_order_btn);
        nameEditText = findViewById(R.id.name_confirm_order);
        phoneEditText = findViewById(R.id.phone_confirm_order);
        cityEditText = findViewById(R.id.city_confirm_order);
        addressEditText = findViewById(R.id.address_confirm_order);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
