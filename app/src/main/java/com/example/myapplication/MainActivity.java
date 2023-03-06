package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Double la;
    Double lang ;
    String u = "https://www.google.com/maps/search/?api=1&quert="+la+","+lang+"";
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView country,city,longitude,latitude;
    Button getLocation, go;
    private  final  static int REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        country=findViewById(R.id.country);
        city=findViewById(R.id.city);
        longitude=findViewById(R.id.longitude);
        latitude=findViewById(R.id.lagitude);
        getLocation = findViewById(R.id.get_location_btn);
        go = findViewById(R.id.getint);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(la != null){
                    Uri requete = Uri.parse(u);
                    Intent intent = new Intent(Intent.ACTION_VIEW, requete);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "You have to get the values of lagi and longtude first", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location !=null){
                                Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses= null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    latitude.setText("Lagitude :" +addresses.get(0).getLatitude());
                                    longitude.setText("Longitude :"+addresses.get(0).getLongitude());
                                    city.setText("City :"+addresses.get(0).getLocality());
                                    country.setText("Country :"+addresses.get(0).getCountryName());
                                    la = addresses.get(0).getLatitude();
                                    lang = addresses.get(0).getLongitude();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }else
        {
            askPermission();
        }
    }
    private void askPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

