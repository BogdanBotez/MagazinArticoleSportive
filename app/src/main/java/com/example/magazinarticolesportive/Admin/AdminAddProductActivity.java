package com.example.magazinarticolesportive.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.magazinarticolesportive.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddProductActivity extends AppCompatActivity
{

    private String Category, Description, ProductName, saveCurrentDate, saveCurrentTime,
            productRandomKey, downloadImageUrl, Size, sport, gender;
    private double Price;
    private int quantity;
    private Button AddProductButton;
    private EditText InputProductName, InputProductDescription, InputProductPrice,
        InputProductSize, InputProductQuantity;
    private ImageView InputProductImage;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductRef;
    private ProgressDialog loadingBar;
    private MaterialSpinner sportSpinner;
    private MaterialSpinner genderSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        //Primeste valoarea dupa cheia "category" , ex: womenTops
        Category = getIntent().getExtras().get("category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        AddProductButton = findViewById(R.id.add_product_btn);
        loadingBar = new ProgressDialog(this);
        InputProductName = findViewById(R.id.product_name);
        InputProductDescription = findViewById(R.id.product_description);
        InputProductPrice = findViewById(R.id.product_price);
        InputProductSize = findViewById(R.id.product_size);
        InputProductImage = findViewById(R.id.input_product_image);
        InputProductQuantity = findViewById(R.id.product_quantity);
        sportSpinner = findViewById(R.id.sport_spinner);
        genderSpinner = findViewById(R.id.gender_spinner);

        genderSpinner.setItems("Male", "Female", "Unisex");
        sportSpinner.setItems("Football", "Hockey", "Basketball", "Tennis", "Handball");

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

        sportSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                sport = item.toString();
            }
        });

        genderSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                gender = item.toString();
            }
        });
    }
    //select imag din galerie

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    //Setez imaginea
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK  && requestCode == GalleryPick && data != null){
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }

    }

    private void ValidateProductData() {
        Description = InputProductDescription.getText().toString();
        Price = Double.parseDouble(InputProductPrice.getText().toString());
        ProductName = InputProductName.getText().toString();
        quantity = Integer.parseInt(InputProductQuantity.getText().toString());
        Size = InputProductSize.getText().toString();

        if(ImageUri == null){
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description)){
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
        }
        else if(Price <= 0){
            Toast.makeText(this, "Please enter a price", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ProductName)){
            Toast.makeText(this, "Please enter a name for the product", Toast.LENGTH_SHORT).show();
        }
        else if(quantity <= 0){
            Toast.makeText(this, "Please enter the quantity", Toast.LENGTH_SHORT).show();
        }
        else{
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {

        loadingBar.setTitle("Adding the product");
        loadingBar.setMessage("Please wait while we are adding the product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child((ImageUri.getLastPathSegment()) + productRandomKey);

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddProductActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddProductActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddProductActivity.this, "Product image's URL saved to database", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }

                    }
                });
            }

            private void SaveProductInfoToDatabase() {
                HashMap<String, Object> productMap = new HashMap<>();
                productMap.put("pid", productRandomKey);
                productMap.put("date", saveCurrentDate);
                productMap.put("time", saveCurrentTime);
                productMap.put("description", Description);
                productMap.put("image", downloadImageUrl);
                productMap.put("category", Category);
                productMap.put("price", Price);
                productMap.put("name", ProductName);
                productMap.put("quantity", quantity);
                productMap.put("size", Size);
                productMap.put("sport", sport);
                productMap.put("gender", gender);

                ProductRef.child(productRandomKey).updateChildren(productMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(AdminAddProductActivity.this, AdminCategoryActivity.class);
                                    startActivity(intent);
                                    loadingBar.dismiss();
                                    Toast.makeText(AdminAddProductActivity.this, "Product has been added successfully", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    loadingBar.dismiss();
                                    String message = uploadTask.getException().toString();
                                    Toast.makeText(AdminAddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
