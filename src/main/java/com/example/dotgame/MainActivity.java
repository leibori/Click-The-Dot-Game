package com.example.dotgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import android.content.SharedPreferences;

import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {
    Button b_dot, b_start;
    TextView tv_score, tv_timeLeft, tv_location, tv_wifiName;
    WebView webView;
    int counterScore = 0;
    private SharedPreferences gamePrefs;
    private int positionX;
    private int positionY;
    String pos,ans, wifiName;
    ArrayList<String> logs;
    public static final String GAME_PREFS = "ArithmeticFile";
    public static final String SHARE_PREFS = "share pre";
    public static final String GPS = "gps";
    public static final String WIFI = "wifi";
    public static final long START_TIME_IN_MILLIS = 10000;
    public static final String UrlCurrency ="http://192.168.8.131:8080/currency";
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;



    private void setButtonRandomPosition(Button button){
        //set a random position in screen to the dot
        View decorView = getWindow().getDecorView();
        int screenWidth = decorView.getWidth();
        int screenHeight = decorView.getHeight();
        int randomX;
        int randomY;

        //make random position with screen
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            randomX = button.getWidth() + (int)(Math.random() * ((screenHeight-(10*button.getWidth())) - button.getWidth()) + 1);
            randomY = button.getHeight() + (int)(Math.random() * ((screenHeight-(10*button.getHeight())) - button.getHeight()) + 1);
        } else {
            // In portrait
            randomX = button.getWidth() + (int)(Math.random() * ((screenWidth-button.getWidth()) - button.getWidth()) + 1);
            randomY = button.getHeight() + (int)(Math.random() * ((screenWidth-button.getHeight()) - button.getHeight()) + 1);
        }

        button.setX(randomX);
        button.setY(randomY);
    }
    private void updateCountDownText() {
        //convert minutes and seconds from millis seconds and set countdown
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tv_timeLeft.setText("Time left: "+timeLeftFormatted);
    }
    private void setHighScore(){
        //set high score
        if(counterScore>0){
        //we have a valid score
            SharedPreferences.Editor scoreEdit = gamePrefs.edit();
            DateFormat dateForm = new SimpleDateFormat("dd MMMM yyyy");
            String dateOutput = dateForm.format(new Date());
            String scores = gamePrefs.getString("highScores", "");
            if(scores.length()>0){
                //we have existing scores
                List<Score> scoreStrings = new ArrayList<Score>();
                String[] exScores = scores.split("\\|");
                for(String eSc : exScores){
                    String[] parts = eSc.split(" - ");
                    scoreStrings.add(new Score(parts[0], Integer.parseInt(parts[1])));
                }
                Score newScore = new Score(dateOutput, counterScore);
                scoreStrings.add(newScore);
                Collections.sort(scoreStrings);
                StringBuilder scoreBuild = new StringBuilder("");
                for(int s=0; s<scoreStrings.size(); s++){
                    if(s>=10) break;//only want ten
                    if(s>0) scoreBuild.append("|");//pipe separate the score strings
                    scoreBuild.append(scoreStrings.get(s).getScoreText());
                }
                //write to prefs
                scoreEdit.putString("highScores", scoreBuild.toString());


            }
            else{
                //no existing scores
                scoreEdit.putString("highScores", ""+dateOutput+" - "+counterScore);
            }
            scoreEdit.commit();

        }
    }
    private void setCordList(int x, int y){
        //add to log list
        Position position =new Position(x,y);
        pos = position.toString();
        logs.add(pos);

    }
    private void saveData(){
        //save list data for shared prefernces using json
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(logs);
        editor.putString("logs",json);
        editor.apply();
    }
    protected void onDestroy(){
        setHighScore();
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b_dot= (Button) findViewById(R.id.b_dot);
        b_start= (Button) findViewById(R.id.b_start);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_timeLeft = (TextView) findViewById(R.id.tv_timeLeft);
        tv_location = (TextView) findViewById(R.id.tv_gps3);
        tv_wifiName =(TextView) findViewById(R.id.tv_wifiName3);
        webView = (WebView) findViewById(R.id.wb_webView);


        b_dot.setEnabled(false);
        gamePrefs = getSharedPreferences(GAME_PREFS, 0);
        logs = new ArrayList<String>();
        SharedPreferences sharedPreferences =getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        ans = sharedPreferences.getString(GPS,"");
        wifiName =sharedPreferences.getString(WIFI,"");
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(UrlCurrency);
        tv_location.setText(ans);
        tv_wifiName.setText(wifiName);


        final CountDownTimer timer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = (long) (millisUntilFinished*0.95);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                b_dot.setEnabled(false);
                b_start.setVisibility(View.VISIBLE);
                tv_timeLeft.setVisibility(View.INVISIBLE);


                saveData();

            }
        };
        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHighScore();
                counterScore =0;
                tv_score.setText("Score: " + counterScore);
                b_dot.setEnabled(true);
                setButtonRandomPosition(b_dot);
                tv_timeLeft.setVisibility(View.VISIBLE);
                timer.start();
                b_start.setVisibility(View.INVISIBLE);
                setCordList(positionX,positionY);
            }
        });
        b_dot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                positionX = (int) motionEvent.getX();
                positionY = (int) motionEvent.getY();
                return false;
            }
        });
        b_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counterScore++;
                tv_score.setText("Score: " + counterScore);
                setButtonRandomPosition(b_dot);
                setCordList(positionX,positionY);
            }
        });




    }
}