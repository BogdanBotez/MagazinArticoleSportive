package com.example.magazinarticolesportive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import java.sql.DatabaseMetaData;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputPhone, InputPassword;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = findViewById(R.id.register_btn);
        InputName = findViewById(R.id.register_name);
        InputPhone = findViewById(R.id.register_phone);
        InputPassword = findViewById(R.id.register_password);
        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String name = InputName.getText().toString();
        String phone = InputPhone.getText().toString();
        String password = InputPassword.getText().toString();

        if(TextUtils.isEmpty(name) || !(name.matches("[a-zA-Z ]*"))){
            InputName.setError("Please type your name.");
        }
        else if(TextUtils.isEmpty(phone) || phone.length() < 10){
            InputPhone.setError("Please type a valid phone number.");
        }
        else if(TextUtils.isEmpty(password) || password.length() < 8){
            InputPassword.setError("The password must contain at least 8 characters.");
        }
        else
        {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhone(name, phone, password);
        }

    }

    private void ValidatePhone(final String name,final String phone, final String password) {
        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone", phone);
                    userDataMap.put("name", name);
                    userDataMap.put("password", password);

                    rootRef.child("Users").child(phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        addCoupon(rootRef, phone);

                                        Toast.makeText(RegisterActivity.this, "Your account has just been created", Toast.LENGTH_SHORT);
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Please check your connection", Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "An account with this phone number already exists.", Toast.LENGTH_SHORT);
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Try again using another phone.", Toast.LENGTH_SHORT);

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addCoupon(DatabaseReference rootRef, String phone) {
        HashMap<String, Object> userCouponsMap = new HashMap<>();
        userCouponsMap.put("name", "NEW10");
        userCouponsMap.put("value", 10);
        rootRef.child("Users").child(phone).child("Coupons").push().updateChildren(userCouponsMap);
    }
}
