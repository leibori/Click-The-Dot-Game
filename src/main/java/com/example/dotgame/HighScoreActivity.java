package com.example.dotgame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.SharedPreferences;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class HighScoreActivity extends AppCompatActivity {
    public static final String SHARE_PREFS = "share pre";
    public static final String GPS = "gps";
    public static final String WIFI = "wifi";
    public static final String UrlCurrency ="http://192.168.8.131:8080/currency";
    WebView webView;


    String ans,wifiName;
    TextView tv_location,tv_wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_high_score);
        TextView scoreView = (TextView)findViewById(R.id.tv_highScore);
        SharedPreferences scorePrefs = getSharedPreferences(MainActivity.GAME_PREFS, 0);
        String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
        StringBuilder scoreBuild = new StringBuilder("");
        for(String score : savedScores){
            scoreBuild.append(score+"\n");
        }
        scoreView.setText(scoreBuild.toString());
        SharedPreferences sharedPreferences =getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        ans = sharedPreferences.getString(GPS,"");
        wifiName = sharedPreferences.getString(WIFI,"");
        tv_location = (TextView) findViewById(R.id.tv_gps4);
        tv_wifi = (TextView) findViewById(R.id.tv_wifiName4);
        tv_location.setText(ans);
        tv_wifi.setText(wifiName);
        webView = (WebView) findViewById(R.id.wb_webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(UrlCurrency);;





    }
}