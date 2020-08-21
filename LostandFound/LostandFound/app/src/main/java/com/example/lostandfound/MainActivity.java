package com.example.lostandfound;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void lost(View view) {
        try {
            Intent i = new Intent(MainActivity.this, Lost.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void found(View view) {
        try {
            Intent n = new Intent(MainActivity.this, Found.class);
            startActivity(n);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}