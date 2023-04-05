package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView txtTemp;
    TextView txtDesc;
    TextView txtCity;
    TextView txtDate;
    ImageView imgWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTemp = (TextView) findViewById(R.id.txtTemp);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtDate = (TextView) findViewById(R.id.txtDate);
        imgWeather = (ImageView) findViewById(R.id.imgWeather);

        txtDate.setText(getCurrentDate());

        String url = "https://api.openweathermap.org/data/2.5/weather?q=Lapu-Lapu,ph&appid=f01e56b126197b3a73960281cf16b8cb&units=Imperial";

        JsonObjectRequest request =  new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //txtTemp.setText("Response: " +response.toString());
                    Log.v("WEATHER", "Response: " +response.toString());

                    try {
                        JSONObject mainJSONObject = response.getJSONObject("main");
                        JSONArray weatherArray = response.getJSONArray("weather");
                        JSONObject firstWeatherObject = weatherArray.getJSONObject(0);


                        String temp = Integer.toString((int)Math.round(mainJSONObject.getDouble("temp")));
                        String weatherDescription = firstWeatherObject.getString("description");
                        String city = response.getString("name");
                        String iconName = firstWeatherObject.getString("icon");

                        txtTemp.setText(temp);
                        txtDesc.setText(weatherDescription);
                        txtCity.setText(city);

                        //int iconResourceId = getResources().getIdentifier("icon_" + weatherDescription.replace(" ",""), "drawable", getPackageName());
                        int iconResourceId = getResources().getIdentifier("icon_" + iconName, "drawable", getPackageName());
                        imgWeather.setImageResource(iconResourceId);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){

                }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        String formattedDate = dateFormat.format(calendar.getTime());

        return formattedDate;
    }
}