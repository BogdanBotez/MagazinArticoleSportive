package com.example.magazinarticolesportive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.magazinarticolesportive.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextView pageTitle, questionTitle;
    private EditText phoneNumber, answerInput;
    private Button verifyBtn;
    private String check = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        check = getIntent().getStringExtra("check");

        phoneNumber = findViewById(R.id.phone_number_reset);
        pageTitle = findViewById(R.id.title_reset);
        questionTitle = findViewById(R.id.question_title_reset);
        answerInput = findViewById(R.id.answer_reset);
        verifyBtn = findViewById(R.id.verify_reset_btn);

    }

    @Override
    protected void onStart() {
        super.onStart();


        phoneNumber.setVisibility(View.GONE);
        if (check.equals("settings")) {
            pageTitle.setText("Security security answer");
            questionTitle.setText("Set an answer for the next security question");
            verifyBtn.setText("Set");

            //displayAnswer();

            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswer();
                }
            });
        } else if (check.equals("login")) {
            phoneNumber.setVisibility(View.VISIBLE);
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyAnswer();
                }
            });
        }
    }

    private void verifyAnswer() {
        final String pNo = phoneNumber.getText().toString();
        final String answer = answerInput.getText().toString();

        if (!pNo.equals("") && !answer.equals("")) {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(pNo);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String phoneDB = dataSnapshot.child("phone").getValue().toString();


                        String answerDB = dataSnapshot.child("answer").getValue().toString();

                        if (!answer.equals(answerDB)) {
                            Toast.makeText(ResetPasswordActivity.this, "The answer is wrong", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                            builder.setTitle("Enter the new password");
                            final EditText newPassword = new EditText(ResetPasswordActivity.this);
                            builder.setView(newPassword);

                            builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!newPassword.getText().toString().equals("")) {
                                        ref.child("password").setValue(newPassword.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(ResetPasswordActivity.this, "The password was changed successfully", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });
                                    }
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
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "You don't have a security answer set.", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Toast.makeText(this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setAnswer() {
        String answer = answerInput.getText().toString().toLowerCase();

        if (answer.equals("")) {
            answerInput.setError("Please type your answer");
        } else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(Prevalent.currentUser.getPhone());

            HashMap<String, Object> userDataMap = new HashMap<>();
            userDataMap.put("answer", answer);

            ref.updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, "Security answer setup completed!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void displayAnswer() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentUser.getPhone());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String answer = dataSnapshot.child("answer").getValue().toString();
                    answerInput.setText(answer);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
