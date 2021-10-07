package com.example.dotgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LoginActivity extends AppCompatActivity  {

    EditText tv_username, tv_password;
    Button bt_login;
    TextView tv_location, tv_wifiName;
    Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;
    WifiManager wifiManager;
    WifiInfo connection;
    WebView webView;

    String gpsLocation,wifiName;
    public static final int REQUEST_LOCATION_PERMISSION =20;
    public static final String UrlCurrency ="http://192.168.8.131:8080/currency";
    public static final String TAG = "MyActivity";

    public void  openActivity(){
        Intent intent = new Intent (this,MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this,
                            "Location permission denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // show gps coordinates and use shared preferences
                                mLastLocation = location;
                                gpsLocation = getString(R.string.location_text,
                                        mLastLocation.getLatitude(),
                                        mLastLocation.getLongitude());
                                tv_location.setText(gpsLocation);
                                sharedPreUse("gps", gpsLocation);
                            } else {
                                tv_location.setText("No Location");
                            }
                        }
                    });
        }
    }
    private void getWifiName(String wifiName){
        // upload from shared preferences and set wifi name
        String wifi ="WIFI: "+wifiName;
        tv_wifiName.setText(wifi);
        sharedPreUse("wifi",wifi);
    }
    private void sharedPreUse(String prefName, String send){
        //generic function for using shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("share pre", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(prefName,send);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bt_login= (Button) findViewById(R.id.b_login);
        tv_password = (EditText) findViewById(R.id.tv_password);
        tv_username = (EditText) findViewById(R.id.tv_username);
        tv_wifiName = (TextView) findViewById(R.id.tv_wifiName);
        webView = (WebView) findViewById(R.id.wb_webView);
        tv_location = (TextView) findViewById(R.id.tv_gps);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(UrlCurrency);
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connection = wifiManager.getConnectionInfo();
        wifiName =connection.getSSID();
        getWifiName(wifiName);
        getLocation();




        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate input
                if (TextUtils.isEmpty(tv_username.getText().toString()) || TextUtils.isEmpty(tv_password.getText().toString())){
                    Toast.makeText(LoginActivity.this,"Invalid Input - Empty data", Toast.LENGTH_LONG).show();
                }
                else if( tv_username.getText().toString().equals(tv_password.getText().toString())){
                    openActivity();

                } else{
                    Toast.makeText(LoginActivity.this,"Invalid Input", Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}