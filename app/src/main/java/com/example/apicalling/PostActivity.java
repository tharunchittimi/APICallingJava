package com.example.apicalling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostActivity extends AppCompatActivity {
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        textView = findViewById(R.id.outputView);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendPostRequest().execute();
            }
        });

    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialogJob;

        protected void onPreExecute() {
            pDialogJob = new ProgressDialog(PostActivity.this, R.style.MyAlertDialogStyle);
            pDialogJob.setMessage("Getting Data...");
            pDialogJob.setIndeterminate(false);
            pDialogJob.setCancelable(true);
            pDialogJob.show();
        }

        protected String doInBackground(String... arg0) {

            HttpURLConnection httpURLConnection;
            URL url;

            try {
                url = new URL("http://dummy.restapiexample.com/api/v1/create");

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("name", "qwertyui");
                jsonParam.put("salary", "22000");
                jsonParam.put("age", "24");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                DataOutputStream printout = new DataOutputStream(outputStream);
                printout.writeBytes(jsonParam.toString());
                printout.flush();
                printout.close();

                int responseCode = httpURLConnection.getResponseCode();

                if (responseCode == 200) {
                    InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    return sb.toString();
                }
                httpURLConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            pDialogJob.dismiss();
            textView.setText(result);
        }
    }
}

