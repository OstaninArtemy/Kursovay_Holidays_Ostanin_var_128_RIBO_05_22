package com.mirea.kt.holidays;

import static android.content.ContentValues.TAG;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText editTextCountryCode;
    private EditText editTextYear;
    private Button buttonSearch;
    private RecyclerView recyclerView;
    private RecycleViewAdapter holidayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextYear = findViewById(R.id.editTextYear);
        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        buttonSearch = findViewById(R.id.buttonSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonSearch.setOnClickListener(v->fetchDataFromAPI());
    }
    private void fetchDataFromAPI() {
        String TextYear = editTextYear.getText().toString();
        String countryCode = editTextCountryCode.getText().toString();

        String urlString = "https://date.nager.at/api/v3/publicholidays/"+TextYear+"/"+countryCode;

        new GetHolidaysTask().execute(urlString);
    }
    private class GetHolidaysTask extends AsyncTask<String,Void,List<HolidaysCountry>> {
        @Override
        protected List<HolidaysCountry> doInBackground(String... urls) {
            List<HolidaysCountry> holidays = new ArrayList<>();
            String urlString = urls[0];
            try {

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                // Читаем ответ от сервера
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                // Чтение данных из  сети с помощью библиотеки JSON и создаем объекты Holiday
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String date = jsonObject.getString("date");
                    holidays.add(new HolidaysCountry(name, date));
                }
            } catch (IOException | JSONException e) {
                Log.e("MainActivity", "Неправильный запрос: " + e.getMessage());
            }
            return holidays;
        }
        @Override
        protected void onPostExecute(List<HolidaysCountry> holidays) {

            holidayAdapter = new RecycleViewAdapter(new ArrayList<>(holidays));
            recyclerView.setAdapter(holidayAdapter);
        }
    }

}