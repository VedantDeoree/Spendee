package com.example.spend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailUser,dateOfCreation,timeOfCreation,signInAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        emailUser=findViewById(R.id.email_account);
        dateOfCreation=findViewById(R.id.dateofCreation);
        timeOfCreation=findViewById(R.id.timeOfCreation);
        signInAt=findViewById(R.id.lastSignInAt);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        emailUser.setText(user.getEmail());

        Long timestampCreate=user.getMetadata().getCreationTimestamp();
        Date date1 = new Date(timestampCreate);
        SimpleDateFormat jdf = new SimpleDateFormat("dd MMM yyyy");
        String java_date = jdf.format(date1);

        SimpleDateFormat jdf1 = new SimpleDateFormat("HH:mm:ss z");
        String TimeOfCreation = jdf1.format(date1);
        dateOfCreation.setText(java_date);
        timeOfCreation.setText(TimeOfCreation);
        Long lastSignInTS=user.getMetadata().getLastSignInTimestamp();

        Date date2 = new Date(lastSignInTS);
        SimpleDateFormat jdf2 = new SimpleDateFormat("dd MMM yyyy    HH:mm:ss z");
        String SignInAt = jdf2.format(date2);
        signInAt.setText(SignInAt);

    }
}