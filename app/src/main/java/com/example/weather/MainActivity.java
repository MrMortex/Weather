package com.example.weather;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText user_field;
    private Button  main_btn;
    private TextView  result_info;
    private TextView result_info1;
    private TextView result_info2;
    private TextView result_info3;
    private TextView result_info4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_field=findViewById(R.id.user_field);
        main_btn=findViewById(R.id.main_btn);
        result_info=findViewById(R.id.result_info);
        result_info1=findViewById(R.id.result_info1);
        result_info2=findViewById(R.id.result_info2);
        result_info3=findViewById(R.id.result_info3);
        result_info4=findViewById(R.id.result_info4);
        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_field.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_LONG).show();
                else{
                    String city= user_field.getText().toString();

                    String  url= "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=7751b15c7e8c7c0ca24bd87d4f90bf63&units=metric";
                    new GetURLData().execute(url);
                }
            }
        });
    }
    private  class GetURLData extends AsyncTask<String,String,String>{
        protected void onPreExecute(){
            super.onPreExecute();
            result_info.setText("Ожидайте...");
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL ulr = new URL(strings[0]);
                connection =(HttpURLConnection) ulr.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line ="";
                while ((line =reader.readLine()) !=null)
                    buffer.append(line).append("\n");
                return  buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection !=null)
                    connection.disconnect();
                try {
                    if (reader != null)

                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return  null;
        }
        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            try {
                JSONObject obj = new JSONObject(result);
                result_info.setText("Температура "+ obj.getJSONObject("main").getInt("temp")+("°C"));
                result_info1.setText("По ощущениям "+ obj.getJSONObject("main").getInt("feels_like")+("°C"));
                result_info2.setText("Скорость ветра "+obj.getJSONObject("wind").getInt("speed")+("м/c"));
                result_info3.setText("Давление "+obj.getJSONObject("main").getInt("pressure")+("Па"));
                result_info4.setText("Влажность "+obj.getJSONObject("main").getInt("humidity")+("%"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}