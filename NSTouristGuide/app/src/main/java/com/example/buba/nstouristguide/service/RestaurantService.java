package com.example.buba.nstouristguide.service;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by pera on 30.11.16..
 */

public class RestaurantService {

    public void saveRestaurant(Object[] urls) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://nstouristguide.webtory.ch/new_restaurant.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("name",(String) urls[0]));
            nameValuePairs.add(new BasicNameValuePair("description", (String) urls[1]));
            nameValuePairs.add(new BasicNameValuePair("address", (String) urls[2]));
            nameValuePairs.add(new BasicNameValuePair("latitude", urls[3].toString()));
            nameValuePairs.add(new BasicNameValuePair("longitude", urls[4].toString()));
            String image_str = Base64.encodeToString((byte[])urls[5], Base64.DEFAULT);
            nameValuePairs.add(new BasicNameValuePair("image", image_str));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRestaurant(Object[] urls) {
        InputStream isr = null;
        String result = "";
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://nstouristguide.webtory.ch/get_restaurant.php?id=" + urls[0]);

        try {

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();

            //convert response to string


        } catch (Exception e) {
            e.printStackTrace();
        }

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

        return result;

    }

}