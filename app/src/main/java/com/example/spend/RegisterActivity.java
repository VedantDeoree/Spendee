package com.example.spend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class RegisterActivity extends AppCompatActivity
{
    private EditText name;
    private EditText phone;
    private EditText email;
    private EditText password;
    private Button register;
    private FirebaseAuth auth;
    // private ProgressDialog progressDialog;
    // FirebaseFirestore fstore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);

        auth = FirebaseAuth.getInstance();
        //  fstore = FirebaseFirestore.getInstance();
        //  progressDialog = new ProgressDialog(this);

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String txt_name = name.getText().toString();
                String txt_email = email.getText().toString();
                String txt_phone = phone.getText().toString();
                String txt_password = password.getText().toString();

                if(TextUtils.isEmpty(txt_name)){
                    name.setError("Name is required");
                    name.requestFocus();
                }
                if(TextUtils.isEmpty(txt_email)){
                    //Toast.makeText(LoginActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                    email.setError("Email is required");
                    email.requestFocus();
                }
                if(TextUtils.isEmpty(txt_phone)){
                    //Toast.makeText(LoginActivity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                    phone.setError("Mobile number is required");
                    phone.requestFocus();
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
                    //registerUser(txt_name, txt_email, txt_password);
                    //  progressDialog.setMessage("Registration in progress");
                    //  progressDialog.setCanceledOnTouchOutside(false);
                    //  progressDialog.show();
                    auth.createUserWithEmailAndPassword(txt_email , txt_password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                                //  progressDialog.dismiss();
                            }
                            else
                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}










