package com.androidman.socialmediaintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button gmail,
//            insta,
            fb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gmail = findViewById(R.id.gmail);
//        insta = findViewById(R.id.insta);
        fb  = findViewById(R.id.fb);

        gmail.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, GmailProfile.class);
            startActivity(i);
        });
//        insta.setOnClickListener(view -> {
//            Intent i = new Intent(MainActivity.this, InstaProfile.class);
//            startActivity(i);
//        });
        fb.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, FacebookProfile.class);
            startActivity(i);
        });
    }
}