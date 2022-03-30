package com.example.apicalling;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ResultsOfRepoList extends AppCompatActivity {
    TextView count, pageNo, apiLimitError;
    Button prev, next;
    JSONArray items;
    int page = 0;
    ListView repoListView;
    String total_count, searchText, incomplete_results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_of_repo_list);
        count = findViewById(R.id.count);
        pageNo = findViewById(R.id.page);
        apiLimitError = findViewById(R.id.apiLimitError);
        prev = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        repoListView = findViewById(R.id.listView);

        searchText = getIntent().getStringExtra("searchText");

        new ATask().execute(searchText + "&page=" + String.valueOf(page + 1));

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteLog.getInstance().saveButtonClick(ResultsOfRepoList.this.getClass().getSimpleName(),"previous");

                page -= 1;
                new ATask().execute(searchText + "&page=" + String.valueOf(page + 1));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteLog.getInstance().saveButtonClick(ResultsOfRepoList.this.getClass().getSimpleName(),"next");
                page += 1;
                new ATask().execute(searchText + "&page=" + String.valueOf(page + 1));
            }
        });
    }

    public class ATask extends AsyncTask<String, Void, Void> {
        private ProgressDialog pDialog;
        boolean apiLimitExceeded = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ResultsOfRepoList.this, R.style.MyAlertDialogStyle);
            pDialog.setMessage("Getting Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection urlConnection;
            URL url;
            InputStream inputStream;
            String response = "";

            try {
                url = new URL("https://api.github.com/search/repositories?q=" + strings[0]);
                WriteLog.getInstance().saveApiData(ResultsOfRepoList.this.getClass().getSimpleName(),"Api https://api.github.com/search/repositories is called");

                Log.e("url Values", url.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                int httpStatus = urlConnection.getResponseCode();

                Log.e("httpstatus", "The response is: " + httpStatus);
                if (httpStatus != HttpURLConnection.HTTP_OK) {
                    inputStream = urlConnection.getErrorStream();

                    Map<String, List<String>> map = urlConnection.getHeaderFields();
                    System.out.println("Printing Response Header...\n");
                    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    }
                } else {
                    inputStream = urlConnection.getInputStream();
                }

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    response += temp;
                }

                Log.e("webApi json object", response);

                if (response.contains("API rate limit exceeded")) {
                    apiLimitExceeded = true;
                } else {
                    JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();
                    items = jsonObject.getJSONArray("items");

                    total_count = jsonObject.getString("total_count");
                    incomplete_results = jsonObject.getString("incomplete_results");
                }
                urlConnection.disconnect();
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
            if (!apiLimitExceeded) {
                apiLimitError.setVisibility(View.INVISIBLE);
                setResultListView();
            } else {
                repoListView.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, new ArrayList<>()));
                apiLimitError.setVisibility(View.VISIBLE);
                count.setText("API Rate Limit Error ! Try After Some Time !");
            }
            pDialog.dismiss();
        }
    }

    private void setResultListView() {
        pageNo.setText("page " + String.valueOf(page + 1));

        if (total_count.equals("0")) {
            count.setText("No Repository Found ! Try Again !");
            count.setTextColor(Color.RED);
            prev.setEnabled(false);
            next.setEnabled(false);
            return;
        }
        if (incomplete_results.equals("true")) {
            count.setText("Total Count:" + String.valueOf(total_count) + "(NetworkError:Incomplete Result!)");
            count.setTextColor(Color.RED);
        } else {
            count.setText("Total Count:" + String.valueOf(total_count));
            count.setTextColor(Color.parseColor("#EA80FC"));
        }

        int tpcount = Integer.parseInt(total_count);
        int totalpage;
        if (tpcount % 6 == 0) {
            totalpage = tpcount / 6 - 1;
        } else {
            totalpage = tpcount / 6;
        }
        Log.e("total page, page", String.valueOf(totalpage) + ", " + String.valueOf(page));

        if (page == 0) {
            prev.setEnabled(false);
        } else {
            prev.setEnabled(true);
        }

        if (page == totalpage) {
            next.setEnabled(false);
        } else {
            next.setEnabled(true);
        }

        List<String> adapterList = new ArrayList<>();
        if (items.length() == 0) {
            return;
        }
        Log.e("some more data", "item.length" + String.valueOf(items.length()));
        for (int i = 0; i < items.length(); i++) {
            JSONObject jo;
            try {
                jo = items.getJSONObject(i);
                adapterList.add(String.valueOf(page * 6 + i + 1) + ". Forks : " + jo.getString("forks") + "\n"
                        + "     Language : " + jo.getString("language") + "\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, adapterList);
        repoListView.setAdapter(adapter);
        repoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ResultsOfRepoList.this, "Repo No." + String.valueOf(page * 6 + 1 + position) + " Selected", Toast.LENGTH_LONG).show();

                Intent i = new Intent(ResultsOfRepoList.this, Repository_details.class);
                try {
                    i.putExtra("repo", items.getJSONObject(position).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(i);
            }
        });
    }
}
