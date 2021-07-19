package com.example.spend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText mail;
    private Button reset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mail=findViewById(R.id.email);
        reset=findViewById(R.id.reset);
        mAuth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailUser=mail.getText().toString().trim();
                if(TextUtils.isEmpty(emailUser)){
                    mail.setError("Email is Required");
                    mail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()){
                    mail.setError("Please provide a valid email");
                    mail.requestFocus();
                    return;
                }
                mAuth.sendPasswordResetEmail(emailUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Please Check your mail for Password reset Instructions", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error! Reset Link is not sent!. "+e.getMessage(), Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                    }
                });
            }
        });
    }
}