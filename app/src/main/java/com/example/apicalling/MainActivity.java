package com.example.apicalling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityLog";
    Button searchButton, another_button, buttonPost;
    EditText searchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchButton = findViewById(R.id.search_button);
        searchName = findViewById(R.id.searchName);
        another_button=findViewById(R.id.another_button);
        buttonPost=findViewById(R.id.buttonPost);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteLog.getInstance().saveButtonClick(MainActivity.this.getClass().getSimpleName(),"Search");
                View view = MainActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                    String txt = searchName.getText().toString();
                    if (txt.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Search Box Can't be Empty !", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!isInternetConnected(getBaseContext())) {
                        Toast.makeText(MainActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    txt += "&per_page=6";

                    Intent intent = new Intent(MainActivity.this, ResultsOfRepoList.class);
                    intent.putExtra("searchText", txt);
                    startActivity(intent);
            }
        });
        another_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this,JobsFinder.class);
                startActivity(i);
            }
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,PostActivity.class);
                startActivity(intent1);
            }
        });
    }

    private boolean isInternetConnected(Context baseContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) baseContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
