package com.example.spend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView nan_git;
    private ImageView cd_git;
    private ImageView nan_link;
    private ImageView cd_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        nan_git = findViewById(R.id.github1);
        cd_git = findViewById(R.id.github2);
        nan_link = findViewById(R.id.linkedin1);
        cd_link = findViewById(R.id.linkedin2);

        cd_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_VIEW);
                intent1.addCategory(Intent.CATEGORY_BROWSABLE);
                intent1.setData(Uri.parse("https://github.com/CDChaitanya"));
                startActivity(intent1);
            }
        });

        cd_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_VIEW);
                intent2.addCategory(Intent.CATEGORY_BROWSABLE);
                intent2.setData(Uri.parse("https://www.linkedin.com/in/chaitanya-deshpande-5a4908190/"));
                startActivity(intent2);
            }
        });

        nan_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_VIEW);
                intent1.addCategory(Intent.CATEGORY_BROWSABLE);
                intent1.setData(Uri.parse("https://github.com/NandiniDeore05"));
                startActivity(intent1);
            }
        });

        nan_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_VIEW);
                intent2.addCategory(Intent.CATEGORY_BROWSABLE);
                intent2.setData(Uri.parse("https://www.linkedin.com/in/nandini-deore-a16799187/"));
                startActivity(intent2);
            }
        });
    }
}