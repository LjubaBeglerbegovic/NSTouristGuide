package com.example.buba.nstouristguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.android.wunderground.api.service.WundergroundApiProvider;
import com.fortysevendeg.android.wunderground.api.service.request.Feature;
import com.fortysevendeg.android.wunderground.api.service.request.Query;
import com.fortysevendeg.android.wunderground.api.service.response.ForecastDayResponse;
import com.fortysevendeg.android.wunderground.api.service.response.ForecastResponse;
import com.fortysevendeg.android.wunderground.api.service.response.HourlyResponse;
import com.fortysevendeg.android.wunderground.api.service.response.SimpleForecastResponse;
import com.fortysevendeg.android.wunderground.api.service.response.TxtForecastResponse;
import com.fortysevendeg.android.wunderground.api.service.response.WundergroundResponse;
import com.google.android.gms.vision.text.Line;

import java.util.List;

import it.restrung.rest.cache.RequestCache;
import it.restrung.rest.client.ContextAwareAPIDelegate;

public class WeatherForecastActivity extends AppCompatActivity {

    private ImageView current;
    private TextView temperature;
    private TextView weather;
    private TextView city;
    private TextView real_feel;
    private TextView humidity;
    private TextView first_high;
    private TextView first_low;
    private TextView first_cond;
    private TextView first_day;
    private TextView second_high;
    private TextView second_low;
    private TextView second_cond;
    private TextView second_day;
    private TextView third_high;
    private TextView third_low;
    private TextView third_cond;
    private TextView third_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        current = (ImageView) findViewById(R.id.imageView1);
        temperature = (TextView) findViewById(R.id.temperature);
        weather = (TextView) findViewById(R.id.weather);
        city = (TextView) findViewById(R.id.city);
        real_feel = (TextView) findViewById(R.id.real_feel);
        humidity = (TextView) findViewById(R.id.humidity);
        first_high = (TextView) findViewById(R.id.first_temp_high);
        first_low = (TextView) findViewById(R.id.first_temp_low);
        first_cond = (TextView) findViewById(R.id.first_cond);
        first_day = (TextView) findViewById(R.id.first_date);
        second_high = (TextView) findViewById(R.id.second_temp_high);
        second_low = (TextView) findViewById(R.id.second_temp_low);
        second_cond = (TextView) findViewById(R.id.second_cond);
        second_day = (TextView) findViewById(R.id.second_date);
        third_high = (TextView) findViewById(R.id.third_temp_high);
        third_low = (TextView) findViewById(R.id.third_temp_low);
        third_cond = (TextView) findViewById(R.id.third_cond);
        third_day = (TextView) findViewById(R.id.third_date);

        WundergroundApiProvider.getClient().query(new ContextAwareAPIDelegate<WundergroundResponse>(WeatherForecastActivity.this, WundergroundResponse.class, RequestCache.LoadPolicy.NEVER) {
            @Override
            public void onResults(WundergroundResponse wundergroundResponse) {
                city.setText("Novi Sad" + ", Serbia");
                real_feel.setText("Real Feel " + wundergroundResponse.getCurrentObservation().getFeelsLikeC().toString());
                temperature.setText(wundergroundResponse.getCurrentObservation().getTempC().toString() + "C");
                weather.setText(wundergroundResponse.getCurrentObservation().getWeather());
                String condition = wundergroundResponse.getCurrentObservation().getWeather();
                if(condition.equals("Clear")){
                    current.setBackgroundResource(R.drawable.clear_sky);
                }else if(condition.equals("Partly cloudy")){
                    current.setBackgroundResource(R.drawable.partly_cloudy);
                }
                humidity.setText("Humidity: " + wundergroundResponse.getCurrentObservation().getRelativeHumidity());
                ForecastResponse forecast = wundergroundResponse.getForecast();
                List<ForecastDayResponse> simple = forecast.getSimpleForecast().getForecastDay();
                first_high.setText(String.valueOf(simple.get(1).getHigh().getCelsius()));
                first_low.setText(String.valueOf(simple.get(1).getLow().getCelsius()));
                first_cond.setText(simple.get(1).getConditions());
                first_day.setText(String.valueOf(simple.get(1).getDate().getWeekday()));
                second_high.setText(String.valueOf(simple.get(2).getHigh().getCelsius()));
                second_low.setText(String.valueOf(simple.get(2).getLow().getCelsius()));
                second_cond.setText(simple.get(2).getConditions());
                second_day.setText(String.valueOf(simple.get(2).getDate().getWeekday()));
                third_high.setText(String.valueOf(simple.get(3).getHigh().getCelsius()));
                third_low.setText(String.valueOf(simple.get(3).getLow().getCelsius()));
                third_cond.setText(simple.get(3).getConditions());
                third_day.setText(String.valueOf(simple.get(3).getDate().getWeekday()));
            }
            @Override
            public void onError(Throwable e) {
                Toast.makeText(WeatherForecastActivity.this, "fail", Toast.LENGTH_LONG).show();
            }
        }, "460ce823711cccf3", Query.latLng(45.251667, 19.836944), Feature.conditions, Feature.forecast10day, Feature.hourly);
    }
}
