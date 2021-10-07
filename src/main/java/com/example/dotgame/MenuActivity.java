package com.example.dotgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;


public class MenuActivity extends AppCompatActivity {
    Button b_play, b_logs, b_hs;
    String ans,wifiAns;
    TextView tv_location, tv_wifiName;
    WebView webView;

    public static final String SHARE_PREFS = "share pre";
    public static final String GPS = "gps";
    public static final String WIFI = "wifi";
    public static final String UrlCurrency ="http://192.168.8.131:8080/currency";


    public void  openPlayActivity(){
        //link to gameplay
        Intent intent = new Intent (this,MainActivity.class);
        startActivity(intent);
    }
    public void  openLogsActivity(){
        //link to logs
        Intent intent = new Intent (this,LogsActivity.class);
        startActivity(intent);
    }
    public void  openHSActivity(){
        //link high score
        Intent intent = new Intent (this,HighScoreActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        b_play= (Button) findViewById(R.id.b_play);
        b_hs= (Button) findViewById(R.id.b_hsPage);
        b_logs= (Button) findViewById(R.id.b_Logs);
        tv_location = (TextView) findViewById(R.id.tv_gps2);
        tv_wifiName = (TextView) findViewById(R.id.tv_wifiName2);
        webView = (WebView) findViewById(R.id.wb_webView);

        SharedPreferences sharedPreferences =getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        ans = sharedPreferences.getString(GPS,"");
        wifiAns = sharedPreferences.getString(WIFI,"");

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(UrlCurrency);
        tv_location.setText(ans);
        tv_wifiName.setText(wifiAns);



        b_play.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openPlayActivity();
            }
        });
        b_hs.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openHSActivity();
            }
        });
        b_logs.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openLogsActivity();
            }
        });

    }
}