package com.example.buba.nstouristguide.custom;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.buba.nstouristguide.R;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayInputStream;
import java.util.List;


public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final List<Integer> ids;
    private final List<String> web;
    private final List<byte[]> imageId;
    private final List<Double> latitude;
    private final List<Double> longitude;
    private final LatLng latLng;

    public CustomList(Activity context,
                      List<String> web, List<Integer> ids, List<byte[]> imageId, List<Double> latitude, List<Double> longitude, LatLng latLng) {
        super(context, R.layout.list_item, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.latLng = latLng;
        this.ids = ids;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null, true);
        TextView txtId = (TextView) rowView.findViewById(R.id.item);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView latLngLog = (TextView) rowView.findViewById(R.id.latLng);

        if(latLng!=null) {
            Location locA = new Location("A");
            locA.setLatitude(latLng.latitude);
            locA.setLongitude(latLng.longitude);
            Location locB = new Location("B");
            locB.setLatitude(latitude.get(position));
            locB.setLongitude(longitude.get(position));
            latLngLog.setText("Distance: " + String.valueOf(String.format("%.2f", locA.distanceTo(locB)/1000)) + "km");

        }
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web.get(position));
        txtId.setText(ids.get(position).toString());


        try{
            byte [] encodeByte=imageId.get(position);
            ByteArrayInputStream imageStream = new ByteArrayInputStream(
                    encodeByte);
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            imageStream.reset();

            imageView.setImageBitmap(theImage);
        }catch (Exception e){
            e.printStackTrace();
        }


        return rowView;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return ids.get(position).toString();
    }
}
