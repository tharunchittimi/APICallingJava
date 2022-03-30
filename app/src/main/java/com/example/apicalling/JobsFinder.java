package com.example.apicalling;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.apicalling.adapters.JobsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobsFinder extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    JSONArray items;
    RecyclerView recyclerView;
    ArrayList<JobsDataItems> jobsDataItems;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_jobs_finder);

        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        recyclerView = findViewById(R.id.rv);
        jobsDataItems = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemOffsetDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        recyclerView.addItemDecoration(itemOffsetDecoration);

        if (!isInternetConnected(getBaseContext())) {
            Toast.makeText(JobsFinder.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
            return;
        }

        new AJTask().execute();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return true;
    }

    public class AJTask extends AsyncTask<String, Void, Void> {
        private ProgressDialog pDialogJob;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogJob = new ProgressDialog(JobsFinder.this, R.style.MyAlertDialogStyle);
            pDialogJob.setMessage("Getting Data...");
            pDialogJob.setIndeterminate(false);
            pDialogJob.setCancelable(true);
            pDialogJob.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection httpURLConnection;
            URL url;
            InputStream inputStream;
            String response = "";

            try {
                url = new URL("https://jobs.github.com/positions.json?description=ruby&page=1");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                int httpStatus = httpURLConnection.getResponseCode();
                Log.e("httpstatus", "The response is: " + httpStatus);

                if (httpStatus != HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getErrorStream();
                    Map<String, List<String>> map = httpURLConnection.getHeaderFields();
                    System.out.println("Printing Response Header...\n");
                    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    }
                } else {
                    inputStream = httpURLConnection.getInputStream();
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    response += temp;
                }

                items = (JSONArray) new JSONTokener(response).nextValue();

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = (JSONObject) items.get(i);
                    String role = item.getString("title");
                    String company = item.getString("company");
                    String city = item.getString("location");
                    String days = item.getString("created_at");
                    String poster = item.getString("company_logo");
                    String description = item.getString("description");
                    String apply = item.getString("how_to_apply");
                    String company_url = item.getString("company_url");
                    JobsDataItems obj = new JobsDataItems(role, company, city, days, poster,description,apply,company_url);
                    jobsDataItems.add(obj);
                }

                httpURLConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialogJob.dismiss();
            JobsAdapter objCustomAdapter1 = new JobsAdapter(jobsDataItems, JobsFinder.this);
            recyclerView.setAdapter(objCustomAdapter1);
        }
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                                   @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
    private boolean isInternetConnected(Context baseContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) baseContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
