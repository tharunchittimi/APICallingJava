package com.example.apicalling;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Repository_details extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_details);

        String repo = getIntent().getStringExtra("repo");
        TextView details = (TextView) findViewById(R.id.Details);
        details.setText(repo);

    }
}
