package com.example.apicalling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ResultJsonData extends AppCompatActivity {

    public static final String EXTRA_ROLE = "role";
    public static final String EXTRA_COMPANY = "company";
    public static final String EXTRA_CITY = "city";
    public static final String EXTRA_DAYS = "days";
    public static final String EXTRA_POSTER = "poster";
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_APPLY = "apply";
    public static final String EXTRA_COMPANY_URL = "company_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_json_data);

        ImageView imageView = findViewById(R.id.imag1);

        Intent i = getIntent();

        String role = i.getStringExtra("role");
        TextView textViewRole = findViewById(R.id.textView8);
        textViewRole.setText(role);

        String company = i.getStringExtra("company");
        TextView textViewCompany = findViewById(R.id.textView12);
        textViewCompany.setText(company);

        String city = i.getStringExtra("city");
        TextView textViewCity = findViewById(R.id.textView13);
        textViewCity.setText(city);

        String days = i.getStringExtra("days");
        TextView textViewDays = findViewById(R.id.textView10);
        textViewDays.setText(days);

        String description = i.getStringExtra("description");
        TextView textViewDiscription = findViewById(R.id.textView11);
        textViewDiscription.setText(description);

        String poster = i.getStringExtra("poster");
        loadBackdrop(poster);

        String apply = i.getStringExtra("apply");
        TextView textViewApply = findViewById(R.id.textView14);
        textViewApply.setText(apply);
        textViewApply.setMovementMethod(LinkMovementMethod.getInstance());

        String company_url = i.getStringExtra("company_url");
        TextView textViewCompany_url = findViewById(R.id.textView15);
        textViewCompany_url.setText(company_url);
        textViewCompany_url.setMovementMethod(LinkMovementMethod.getInstance());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadBackdrop(String poster) {
        ImageView resultImg = findViewById(R.id.resultImg);
        Glide.with(this).load(poster).into(resultImg);
    }
}
