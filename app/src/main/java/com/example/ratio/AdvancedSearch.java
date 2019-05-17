package com.example.ratio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AdvancedSearch extends AppCompatActivity {
    public static final String RESULT = "RESULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
        getSupportActionBar().setTitle("Advanced");
    }
}
