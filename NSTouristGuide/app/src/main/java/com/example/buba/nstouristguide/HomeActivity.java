package com.example.buba.nstouristguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button weatherForecast;
    private Button map;
    private Button listHS;
    private Button listR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        weatherForecast = (Button) findViewById(R.id.btn_weather);
        map = (Button) findViewById(R.id.btn_map);
        listHS = (Button) findViewById(R.id.btn_hs);
        listR = (Button) findViewById(R.id.btn_restaurants);

        weatherForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weather = new Intent(getApplicationContext(), WeatherForecastActivity.class);
                startActivity(weather);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapNS = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(mapNS);
            }
        });

        listHS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listHs = new Intent(getApplicationContext(), ListHistoricalSightsActivity.class);
                startActivity(listHs);
            }
        });

        listR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent listR = new Intent(getApplicationContext(), ListRestaurantsActivity.class);
                startActivity(listR);
            }
        });
    }
}
