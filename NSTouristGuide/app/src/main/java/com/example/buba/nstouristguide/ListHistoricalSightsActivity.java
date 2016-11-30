package com.example.buba.nstouristguide;

import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.buba.nstouristguide.custom.CustomList;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class ListHistoricalSightsActivity extends AppCompatActivity {

    private List<String> Data = new ArrayList<>();
    private List<Integer> ids = new ArrayList<>();
    private List<Double> latitude = new ArrayList<>();
    private List<Double> longitude = new ArrayList<>();
    private LatLng latLng;
    private List<byte[]> images = new ArrayList<>();
    String result;
    InputStream isr;
    LocationListener mLocationListener;
    LocationManager mLocationManager;
    Location myLoc;

    private GridView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        new getData().execute();
    }

    private class getData extends AsyncTask<String, Void, String> {
        String name;

        @Override
        protected String doInBackground(String... params) {
            result = "";
            isr = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://nstouristguide.webtory.ch/get_sights.php"); //YOUR PHP SCRIPT ADDRESS
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                isr = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());

            }


            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                isr.close();

                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error  converting result " + e.toString());
            }


            try {

                if(result!=null) {
                    JSONArray jArray = new JSONArray(result);

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        ids.add(json.getInt("id"));
                        Data.add(json.getString("name"));
                        byte[] encodeByte = Base64.decode(json.getString("image"), Base64.DEFAULT);
                        images.add(encodeByte);
                        latitude.add(json.getDouble("latitude"));
                        longitude.add(json.getDouble("longitude"));

                    }
                }

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error Parsing Data " + e.toString());
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                Criteria criteria = new Criteria();
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                myLoc= mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                mLocationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        CustomList adapter = new
                                CustomList(ListHistoricalSightsActivity.this, Data, ids, images, latitude, longitude, latLng);
                        list=(GridView) findViewById(R.id.gridView1);
                        list.setAdapter(adapter);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {}

                    @Override
                    public void onProviderEnabled(String s) {}

                    @Override
                    public void onProviderDisabled(String s) {}
                };
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                        0, mLocationListener);


            }catch (SecurityException e){
                e.printStackTrace();
            }
            LatLng test;
            if(myLoc!=null) {
                test = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
            }else{
                test = null;
            }
            final CustomList adapter = new
                    CustomList(ListHistoricalSightsActivity.this, Data, ids, images, latitude, longitude, test);
            list = (GridView) findViewById(R.id.gridView1);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String id = (String) adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(getApplicationContext(), HistoricalSightDetailsActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
