package com.example.weathercastapp;

import android.view.View;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Scanner;

public class Wetter extends MainActivity {
    double temperatur;
    double windgeschwindigkeit;
    boolean regen;
    boolean sonne;


    protected void wetter(String city){
        Scanner scan = new Scanner(System.in);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder sb = new StringBuilder();
                String var = null;

                try{
                    //Connection
                    final String apikey = "acef0af6fbedbab3e96618e6c95910ac";
                    Document doc = Jsoup.connect("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apikey)
                            .ignoreContentType(true).get();

                    //Inhalt in complete
                    String complete = doc.text()
                            .replaceAll(",","\n");
                    String[] arr = complete.split("\n");

                    String weather_temperatur = arr[7];
                    weather_temperatur = weather_temperatur.substring(15);
                    double temperatur = formeln.ktoc(Double.parseDouble(weather_temperatur));

                    sb.append(temperatur).append("°C\n");

                    String weather_description = arr[4];
                    weather_description = weather_description.substring(15).replaceAll("\"","");

                    sb.append(weather_description).append("\n");

                    for(int i = 0; i < arr.length; i++) {
                        if(arr[i].contains("country")) {
                            String land = arr[i];
                            land = land.replaceAll("\"", "");
                            sb.append(land).append("\n");
                        }else if(arr[i].contains("name")){
                            String name = arr[i];
                            name = name.replaceAll("\"", "");
                            sb.append(name).append("\n");
                        }
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(sb.toString().contains("rain")) {
                            Toast.makeText(Wetter.this, "Its raining", Toast.LENGTH_SHORT).show();
                            iV_rainy.setVisibility(View.VISIBLE);
                        }else if(sb.toString().contains("sun")){
                            iV_sunny.setVisibility(View.VISIBLE);
                        }else if(sb.toString().contains("cloud")){
                            iV_cloudy.setVisibility(View.VISIBLE);
                        }else if(sb.toString().contains("wind")){
                            iV_windy.setVisibility(View.VISIBLE);
                        }
                        tv_result.setText(sb);
                    }
                });
            }
        }).start();
    }

}
