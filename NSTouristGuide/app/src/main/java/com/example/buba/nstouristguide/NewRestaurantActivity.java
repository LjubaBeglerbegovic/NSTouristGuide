package com.example.buba.nstouristguide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.buba.nstouristguide.service.HSService;
import com.example.buba.nstouristguide.service.RestaurantService;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
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

public class NewRestaurantActivity extends AppCompatActivity {

    private EditText name;
    private EditText description;
    private EditText address;
    LatLng latLng;

    private final int image = 1;
    private ImageView img;

    private Button btnChooseImages;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_restaurant);

        name = (EditText) findViewById(R.id.r_name);
        description = (EditText) findViewById(R.id.r_description);
        address = (EditText) findViewById(R.id.r_address);
        latLng = (LatLng) getIntent().getExtras().get("map");
        img = (ImageView) findViewById(R.id.r_img);

        btnChooseImages = (Button) findViewById(R.id.btnUploadPicR);
        btnSave = (Button) findViewById(R.id.btnSaveR);

        btnChooseImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select images"),image);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Name = name.getText().toString();
                final String Description = description.getText().toString();
                final String Address = address.getText().toString();
                final Bitmap image=((BitmapDrawable)img.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 90, stream); //compress to which format you want.
                byte [] byte_arr = stream.toByteArray();
                new Send().execute(Name, Description, Address, latLng.latitude, latLng.longitude, byte_arr);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == image && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    img.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public class Send extends AsyncTask<Object, Void,Long > {

        @Override
        protected Long doInBackground(Object... urls) {
            new RestaurantService().saveRestaurant(urls);

            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(intent);
            return null;
        }


        @Override
        protected void onPreExecute(){

        }
    }
}
