package com.mirea.kt.holidays;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ActivaitLogin extends AppCompatActivity {
    private EditText loginEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activait_login);

        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String login = loginEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(ActivaitLogin.this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
                return;
            }

            new AuthTask().execute(login, password);
        });
    }

    private class AuthTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            String login = params[0];
            String password = params[1];
            String group = "RIBO-05-22";
            JSONObject jsonResponse = null;

            try {
                URL url = new URL("https://android-for-students.ru/coursework/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String postData = "lgn=" + URLEncoder.encode(login, "UTF-8")
                        + "&pwd=" + URLEncoder.encode(password, "UTF-8")
                        + "&g=" + URLEncoder.encode(group, "UTF-8");

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                jsonResponse = new JSONObject(response.toString());
                conn.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            if (jsonResponse != null) {
                try {
                    int resultCode = jsonResponse.getInt("result_code");
                    if (resultCode == 1) {

                        Toast.makeText(ActivaitLogin.this, "Авторизация успешна", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivaitLogin.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ActivaitLogin.this, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ActivaitLogin.this, "Ошибка обработки ответа", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ActivaitLogin.this, "Ошибка соединения", Toast.LENGTH_SHORT).show();
            }
        }
    }
}