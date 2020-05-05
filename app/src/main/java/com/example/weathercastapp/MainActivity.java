package com.example.weathercastapp;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
TextView tv_result,tv_temperatur,tv_wind,tv_sonne,tv_land;
View lay_const, lay_aussen, lay_symbole;
Button btn_start;
EditText et_input;
ImageView iV_sunny, iV_rainy, iV_cloudy, iV_windy, iv_windleaves, iv_windleaves2
                    ,iv_windleaves3, iv_windleaves4, iv_windleaves5;
double temperatur;
double weatherspeed;
ObjectAnimator colorFade = ObjectAnimator.ofObject(lay_const, "backgroundColor", new ArgbEvaluator(),
                                        Color.argb(255,255,255,255), 0xff000000);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
            btn_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // Wetter wetterbericht = new Wetter();
                       // wetterbericht.getTemperatur(et_input.getText().toString());
                        getTemperatur(et_input.getText().toString());
                        getDescription(et_input.getText().toString());
                        getWind(et_input.getText().toString());
                        getLand(et_input.getText().toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    private void setupUI(){
        tv_result = findViewById(R.id.tv_temperatur);
        btn_start=findViewById(R.id.btn_start);
        et_input = findViewById(R.id.et_input);
        iV_cloudy = findViewById(R.id.iV_cloudy);
        iV_rainy = findViewById(R.id.iv_regen);
        iV_sunny = findViewById(R.id.iv_sunrain);
       // iV_windy = findViewById(R.id.iV_windy);
        tv_temperatur = findViewById(R.id.tv_temperatur);
        tv_wind = findViewById(R.id.tv_wind);
        tv_sonne = findViewById(R.id.tv_beschreibung);
        tv_land = findViewById(R.id.tv_land);
       // tv_regen = findViewById(R.id.tv_regen);
        lay_const = findViewById(R.id.const_layout);
        lay_aussen = findViewById(R.id.linearLayout2);
        iv_windleaves = findViewById(R.id.iv_windleaves);
        iv_windleaves2 = findViewById(R.id.iv_windleaves2);
        iv_windleaves3 = findViewById(R.id.iv_windleaves3);
        iv_windleaves4 = findViewById(R.id.iv_windleaves4);
        iv_windleaves5 = findViewById(R.id.iv_windleaves5);




    }

    private void setInvisibility(){
        //iV_windy.setVisibility(View.INVISIBLE);
        iV_cloudy.setVisibility(View.GONE);
        iV_rainy.setVisibility(View.GONE);
        //iV_sunny.setVisibility(View.INVISIBLE);
        iv_windleaves.setVisibility(View.GONE);
        iv_windleaves2.setVisibility(View.GONE);
        iv_windleaves3.setVisibility(View.GONE);
        iv_windleaves4.setVisibility(View.GONE);
        iv_windleaves5.setVisibility(View.GONE);
    }

    protected void getTemperatur(String city){
        setInvisibility();
        new Thread(new Runnable() {
            @Override
            public void run() {
            try {
                //Connection
                final String apikey = "acef0af6fbedbab3e96618e6c95910ac";
                Document doc = Jsoup.connect("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey)
                        .ignoreContentType(true).get();
                //Inhalt in complete
                String complete = doc.text()
                        .replaceAll(",", "\n");
                Log.println(1,"TAG", "Bis hier hin");
                String[] arr = complete.split("\n");
                String weather_temperatur = arr[7];
                weather_temperatur = weather_temperatur.substring(15);
                temperatur = formeln.ktoc(Double.parseDouble(weather_temperatur));
            }catch(Exception e){
                e.printStackTrace();
            }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Layout nach Temperatur ändern
                        tv_temperatur.setText(temperatur +"°C");
                        if(temperatur>20.0){
                            //iV_sunny.setVisibility(View.VISIBLE);
                            colorFade.setDuration(16000);
                            colorFade.start();
                            lay_const.setBackgroundColor(Color.parseColor("#FFC107"));
                        }else if(temperatur <10){
                            colorFade.setDuration(16000);
                            colorFade.start();
                            //lay_aussen.setBackgroundColor(Color.parseColor("#03A9F4"));
                            lay_const.setBackgroundColor(Color.parseColor("#03A9F4"));
                        }else{
                            colorFade.setDuration(16000);
                            colorFade.start();
                            //lay_aussen.setBackgroundColor(Color.parseColor("#8BC34A"));
                            lay_const.setBackgroundColor(Color.parseColor("#8BC34A"));
                        }
                    }
                    });
            }}).start();

       }

    protected void getDescription(String city){
        setInvisibility();
     StringBuilder sb = new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Connection
                    final String apikey = "acef0af6fbedbab3e96618e6c95910ac";
                    Document doc = Jsoup.connect("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey)
                            .ignoreContentType(true).get();
                    //Inhalt in complete
                    String complete = doc.text()
                            .replaceAll(",", "\n");
                    Log.println(1,"TAG", "Bis hier hin");
                    String[] arr = complete.split("\n");
                    String weather_description = arr[4];
                    weather_description = weather_description.substring(15).replaceAll("\"","");
                    sb.append(weather_description);
                }catch(Exception e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_sonne.setText(sb);
                        if(sb.toString().contains("rain")){
                            iV_rainy.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.FadeIn).duration(3000).playOn(iV_rainy);
                        }else if(sb.toString().contains("cloud")){
                            iV_cloudy.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.FadeIn).duration(3000).playOn(iV_cloudy);
                        }
                    }
                });
            }}).start();

    }

    protected void getWind(String city){
        setInvisibility();
        StringBuilder sb = new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Connection
                    final String apikey = "acef0af6fbedbab3e96618e6c95910ac";
                    Document doc = Jsoup.connect("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey)
                            .ignoreContentType(true).get();
                    //Inhalt in complete
                    String complete = doc.text()
                            .replaceAll(",", "\n");
                    Log.println(1,"TAG", "Bis hier hin");
                    String[] arr = complete.split("\n");
                    String weather_description = arr[14];
                    weather_description = weather_description.substring(16);
                   weatherspeed = Double.parseDouble(weather_description);
                    weatherspeed = formeln.mstokmh(weatherspeed);
                    sb.append(weatherspeed).append("km/h");
                }catch(Exception e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_wind.setText(sb);
                        if(weatherspeed>15.0){
                            iv_windleaves.setVisibility(View.VISIBLE);
                            iv_windleaves2.setVisibility(View.VISIBLE);
                            iv_windleaves3.setVisibility(View.VISIBLE);
                            iv_windleaves4.setVisibility(View.VISIBLE);
                            iv_windleaves5.setVisibility(View.VISIBLE);

                            YoYo.with(Techniques.SlideInLeft).onStart(animator -> YoYo.with(Techniques.Swing)
                                    .onStart(animator2 -> YoYo.with(Techniques.FadeOut)
                                            .duration(6200).repeat(3)
                                            .playOn(iv_windleaves)).duration(6300).repeat(3).playOn(iv_windleaves))
                                    .duration(3300)
                                    .repeat(3)
                                    .playOn(iv_windleaves);

                            YoYo.with(Techniques.SlideInLeft).onStart(animator -> YoYo.with(Techniques.Swing)
                                    .onStart(animator2 -> YoYo.with(Techniques.FadeOut)
                                            .duration(6200).repeat(3)
                                            .playOn(iv_windleaves2)).duration(6200).repeat(3).playOn(iv_windleaves2))
                                    .duration(3800)
                                    .repeat(3)
                                    .playOn(iv_windleaves2);

                            YoYo.with(Techniques.SlideInLeft).onStart(animator -> YoYo.with(Techniques.Swing)
                                    .onStart(animator2 -> YoYo.with(Techniques.FadeOut)
                                            .duration(6200).repeat(3)
                                            .playOn(iv_windleaves3)).duration(6400).repeat(3).playOn(iv_windleaves3))
                                    .duration(3700)
                                    .repeat(3)
                                    .playOn(iv_windleaves3);

                            YoYo.with(Techniques.SlideInLeft).onStart(animator -> YoYo.with(Techniques.Wobble)
                                    .onStart(animator2 -> YoYo.with(Techniques.FadeOut)
                                            .duration(6200).repeat(3)
                                            .playOn(iv_windleaves4)).duration(6500).repeat(3).playOn(iv_windleaves4))
                                    .duration(1600)
                                    .repeat(3)
                                    .playOn(iv_windleaves4);

                            YoYo.with(Techniques.SlideInLeft).onStart(animator -> YoYo.with(Techniques.Swing)
                                    .onStart(animator2 -> YoYo.with(Techniques.FadeOut)
                                            .duration(6200).repeat(3)
                                            .playOn(iv_windleaves5)).duration(4200).repeat(4).playOn(iv_windleaves5))
                                    .duration(3400)
                                    .repeat(3)
                                    .playOn(iv_windleaves5);
                        }
                    }
                });
            }}).start();

    }

    protected void getLand(String city){
        setInvisibility();
        StringBuilder sb = new StringBuilder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wait(30);
                    //Connection
                    final String apikey = "acef0af6fbedbab3e96618e6c95910ac";
                    Document doc = Jsoup.connect("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey)
                            .ignoreContentType(true).get();
                    //Inhalt in complete
                    String complete = doc.text()
                            .replaceAll(",", "\n");
                    Log.println(1,"TAG", "Bis hier hin");
                    String[] arr = complete.split("\n");
                    String country = arr[20];
                    country = country.substring(10).replaceAll("\"","");
                    sb.append(country);
                }catch(Exception e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_land.setText(sb);
                    }
                });
            }}).start();

    }

    }






