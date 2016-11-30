package com.example.buba.nstouristguide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buba.nstouristguide.service.HSService;
import com.example.buba.nstouristguide.service.RestaurantService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private TextView name;
    private TextView description;
    private TextView address;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        new Send().execute(id);
    }

    public class Send extends AsyncTask<Object, Void,String > {
        String restaurant;
        @Override
        protected String doInBackground(Object... urls) {
            restaurant = new RestaurantService().getRestaurant(urls);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            name = (TextView) findViewById(R.id.r_name_det);
            description = (TextView) findViewById(R.id.r_description_det);
            address = (TextView) findViewById(R.id.r_address_det);
            image = (ImageView) findViewById(R.id.image_det_r);
            try{
                JSONArray jArray = new JSONArray(restaurant);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject object = jArray.getJSONObject(i);
                    Log.e("AAAAA", object.toString());
                    name.setText(object.getString("name"));
                    description.setText(object.getString("description"));
                    address.setText(object.getString("address"));
                    byte[] encodeByte = Base64.decode(object.getString("image"), Base64.DEFAULT);
                    ByteArrayInputStream imageStream = new ByteArrayInputStream(
                            encodeByte);
                    Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                    imageStream.reset();

                    image.setImageBitmap(theImage);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }


        @Override
        protected void onPreExecute(){

        }
    }
}
