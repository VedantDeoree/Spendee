package com.example.spend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private EditText email;
    private EditText password;
    private Button login;

    private TextView forgot;
    private FirebaseAuth auth;
    // private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forgot = findViewById(R.id.forgot);
        auth = FirebaseAuth.getInstance();
        // progressDialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                if(TextUtils.isEmpty(txt_email)){
                    //Toast.makeText(LoginActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                    email.setError("Email is required");
                    email.requestFocus();
                }
                if(TextUtils.isEmpty(txt_password)){
                    password.setError("Password is required");
                    password.requestFocus();
                }
                if(txt_password.length() < 6){
                    password.setError("Minimum password length is 6 characters");
                    password.requestFocus();
                }
                else{
                    // progressDialog.setMessage("Login in progress");
                    // progressDialog.setCanceledOnTouchOutside(false);
                    // progressDialog.show();
                    loginUser(txt_email , txt_password);}
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ResetPassword.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password)
    {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    if(user.isEmailVerified()){
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                       finish();
//                    }
//                    else{
//                        Toast.makeText(LoginActivity.this, "Register First", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    //  progressDialog.dismiss();
                } else
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


}