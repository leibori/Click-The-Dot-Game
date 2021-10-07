package com.example.dotgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LogsActivity extends AppCompatActivity {
    TextView tv_location, tv_wifi;
    WebView webView;
    ListView lv_logs;
    String ans,wifiName;
    ArrayList<String> logs;

    public static final String SHARE_PREFS = "share pre";
    public static final String GPS = "gps";
    public static final String WIFI = "wifi";
    public static final String UrlCurrency ="http://192.168.8.131:8080/currency";



    private void loadData(){
        //load data from share preferences for list
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("logs",null);
        Type type =  new TypeToken<ArrayList<String>>(){}.getType();
        logs =gson.fromJson(json,type);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        lv_logs = (ListView) findViewById(R.id.ls_logs);
        logs = new ArrayList<String>();
        loadData();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.log_list,logs);
        lv_logs.setAdapter(adapter);
        SharedPreferences sharedPreferences =getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        ans = sharedPreferences.getString(GPS,"");
        wifiName = sharedPreferences.getString(WIFI,"");
        tv_location = (TextView) findViewById(R.id.tv_gps5);
        tv_wifi = (TextView) findViewById(R.id.tv_wifiName5);
        tv_location.setText(ans);
        tv_wifi.setText(wifiName);
        webView = (WebView) findViewById(R.id.wb_webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(UrlCurrency);



    }
}