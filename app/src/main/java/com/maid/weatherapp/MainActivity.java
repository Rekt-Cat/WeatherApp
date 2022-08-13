package com.maid.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import adapter.adapter;
import adapter.model;

public class MainActivity extends AppCompatActivity {

    double lon; // current longitude of user
    double lat; // current latitude of user
    String cityName = "Not Found!";
    RecyclerView recyclerView;
    ConstraintLayout constraintLayout;
    ArrayList<model> list = new ArrayList<>();



    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        constraintLayout = (ConstraintLayout) findViewById(R.id.con);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        try {
                            List<Address> adress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            lon = adress.get(0).getLongitude();
                            lat = adress.get(0).getLatitude();
                            Log.d("Thread1", "The main thread is : " + Thread.currentThread().getName());
                            String locality = getLocality(lon, lat);
                            apicalling(locality);
                            Log.d("lol", "The longitude is 2 : " + String.valueOf(lon) + " The value of latitude is 2 : " + String.valueOf(lat));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


    }

    String getLocality(double longitude, double latitude) {
        Geocoder g = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> address = g.getFromLocation(latitude, longitude, 1);
            for (Address a : address) {
                if (a != null) {
                    String city = a.getLocality();
                    if (city != null && !city.equals("")) {
                        cityName = city;
                    } else {
                        Log.d("lol", "City not found!");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }


    void apicalling(String cityName) {
        Date date = new Date();
        int currDate = date.getHours();
        Handler handler1 = new Handler(getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                String time2 = msg.getData().getString("time2");
                String tempc= msg.getData().getString("tempc");
                String windspeed= msg.getData().getString("windspeed");
                list.add(new model(time2,tempc,windspeed));
                Log.d("list","The list is : "+list);
                SpacingDecor spacingDecor = new SpacingDecor(1,1);
                adapter adapter = new adapter(list,MainActivity.this);
                recyclerView.addItemDecoration(spacingDecor);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager linearLayoutManager =new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
                recyclerView.setLayoutManager(linearLayoutManager);

            }
        };
        Handler handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d("thread1","The thread of the handler is : "+ Thread.currentThread().getName());

                constraintLayout = findViewById(R.id.con);

                ImageView icon = findViewById(R.id.icon);
                TextView condi = findViewById(R.id.condi);

                TextView stateName = findViewById(R.id.stateName);
                TextView currtemp = findViewById(R.id.currTemp);
                TextView currRealFeel = findViewById(R.id.realFeel);
                TextView country= findViewById(R.id.country);

                TextView sun = findViewById(R.id.sunrise);
                TextView moon = findViewById(R.id.moonrise);
                TextView sunS =findViewById(R.id.sunset2);
                TextView moonS = findViewById(R.id.moonset);

                sun.setText(msg.getData().getString("sunRise"));
                moon.setText(msg.getData().getString("moonRise"));
                sunS.setText(msg.getData().getString("sunSet"));
                moonS.setText(msg.getData().getString("moonSet"));

                stateName.setText(msg.getData().getString("stateName"));
                currtemp.setText(msg.getData().getString("currTemp")+"°C");
                currRealFeel.setText(msg.getData().getString("currRealFeel1")+"/"+msg.getData().getString("currRealFeel2")+"°C");
                country.setText(msg.getData().getString("country"));

                if(currDate >= 4 && currDate <= 8&&msg.getData().getInt("rain")==0){
                    constraintLayout.setBackgroundResource(R.drawable.dawn);
                    if(msg.getData().getString("conditon").equals("Partly cloudy")){
                        icon.setImageResource(R.drawable.partiallycloudedmor);
                        condi.setText("Partly cloudy");
                    }
                    else if(msg.getData().getString("conditon").equals("Light rain shower")){
                        icon.setImageResource(R.drawable.lightrainshowermor);
                        condi.setText("Light rain shower");
                    }
                    else if(msg.getData().getString("conditon").equals("Clear")){
                        icon.setImageResource(R.drawable.clearmor);
                        condi.setText("Clear");
                    }
                    else if(msg.getData().getString("conditon").equals("Heavy rain")){
                        icon.setImageResource(R.drawable.heavyrain);
                        condi.setText("Heavy rain");
                    }
                    else if(msg.getData().getString("conditon").equals("Moderate rain")){
                        icon.setImageResource(R.drawable.rain);
                        condi.setText("Moderate rain");
                    }
                }
                else if(currDate >= 4 && currDate <= 8&&msg.getData().getInt("rain")!=0){
                    constraintLayout.setBackgroundResource(R.drawable.rain);
                }
                else if(currDate > 8 && currDate < 17 && msg.getData().getInt("rain")!=0){
                    constraintLayout.setBackgroundResource(R.drawable.rain);
                }
                else if (currDate > 8 && currDate < 17 && msg.getData().getInt("rain")==0) {
                    constraintLayout.setBackgroundResource(R.drawable.sky);
                    if(msg.getData().getString("conditon").equals("Partly cloudy")){
                        icon.setImageResource(R.drawable.partiallycloudedmor);
                        condi.setText("Partly cloudy");
                    }
                    else if(msg.getData().getString("conditon").equals("Light rain shower")){
                        icon.setImageResource(R.drawable.lightrainshowermor);
                        condi.setText("Light rain shower");
                    }
                    else if(msg.getData().getString("conditon").equals("Clear")||msg.getData().getString("conditon").equals("Sunny")){
                        icon.setImageResource(R.drawable.clearmor);
                        condi.setText("Clear");
                    }
                    else if(msg.getData().getString("conditon").equals("Heavy rain")){
                        icon.setImageResource(R.drawable.heavyrain);
                        condi.setText("Heavy rain");
                    }
                    else if(msg.getData().getString("conditon").equals("Moderate rain")){
                        icon.setImageResource(R.drawable.rain);
                        condi.setText("Moderate rain");
                    }
                }
                else if(currDate>=17&&currDate<=19&&msg.getData().getInt("rain")!=0){
                    constraintLayout.setBackgroundResource(R.drawable.duskrain);
                }
                else if(currDate>=17&&currDate<=19){
                    constraintLayout.setBackgroundResource(R.drawable.dusk1);
                    if(msg.getData().getString("conditon").equals("Partly cloudy")){
                        icon.setImageResource(R.drawable.partiallycloudedmor);
                        condi.setText("Partly cloudy");
                    }
                    else if(msg.getData().getString("conditon").equals("Light rain shower")){
                        icon.setImageResource(R.drawable.lightrainshowermor);
                        condi.setText("Light rain shower");
                    }
                    else if(msg.getData().getString("conditon").equals("Clear")||msg.getData().getString("conditon").equals("Sunny")){
                        icon.setImageResource(R.drawable.clearmor);
                        condi.setText("Clear");
                    }
                    else if(msg.getData().getString("conditon").equals("Heavy rain")){
                        icon.setImageResource(R.drawable.heavyrain);
                        condi.setText("Heavy rain");
                    }
                    else if(msg.getData().getString("conditon").equals("Moderate rain")){
                        icon.setImageResource(R.drawable.rain);
                        condi.setText("Moderate rain");
                    }
                }
                else if ( currDate > 19&& currDate<=23 && msg.getData().getInt("rain")!=0) {
                    constraintLayout.setBackgroundResource(R.drawable.nskyrain);
                }
                else if ( currDate > 19&& currDate<=23 &&msg.getData().getInt("rain")==0) {
                    constraintLayout.setBackgroundResource(R.drawable.nsky);
                    if(msg.getData().getString("conditon").equals("Partly cloudy")){
                        icon.setImageResource(R.drawable.partiallycloudednight);
                        condi.setText("Partly cloudy");
                    }
                    else if(msg.getData().getString("conditon").equals("Light rain shower")){
                        icon.setImageResource(R.drawable.lightrainshower);
                        condi.setText("Light rain shower");
                    }
                    else if(msg.getData().getString("conditon").equals("Clear")||msg.getData().getString("conditon").equals("Sunny")){
                        icon.setImageResource(R.drawable.clearnight);
                        condi.setText("Clear");
                    }
                    else if(msg.getData().getString("conditon").equals("Heavy rain")){
                        icon.setImageResource(R.drawable.heavyrain);
                        condi.setText("Heavy rain");
                    }
                    else if(msg.getData().getString("conditon").equals("Moderate rain")){
                        icon.setImageResource(R.drawable.rain);
                        condi.setText("Moderate rain");
                    }

                }
                else if(currDate >=0 && currDate<4&& msg.getData().getInt("rain")!=0){
                    constraintLayout.setBackgroundResource(R.drawable.nskyrain);
                    icon.setImageResource(R.drawable.rainy);
                }
                else if(currDate >=0 && currDate<4&&msg.getData().getInt("rain")==0 ){
                    constraintLayout.setBackgroundResource(R.drawable.nsky);
                    if(msg.getData().getString("conditon").equals("Partly cloudy")){
                        icon.setImageResource(R.drawable.partiallycloudednight);
                        condi.setText("Partly cloudy");
                    }
                    else if(msg.getData().getString("conditon").equals("Light rain shower")){
                        icon.setImageResource(R.drawable.lightrainshower);
                        condi.setText("Light rain shower");
                    }
                    else if(msg.getData().getString("conditon").equals("Clear")||msg.getData().getString("conditon").equals("Sunny")){
                        icon.setImageResource(R.drawable.clearnight);
                        condi.setText("Clear");
                    }
                    else if(msg.getData().getString("conditon").equals("Heavy rain")){
                        icon.setImageResource(R.drawable.heavyrain);
                        condi.setText("Heavy rain");
                    }
                    else if(msg.getData().getString("conditon").equals("Moderate rain")){
                        icon.setImageResource(R.drawable.rain);
                        condi.setText("Moderate rain");
                    }
                }














            }
        };




        String url = "https://api.weatherapi.com/v1/forecast.json?key=89ee4513fd284b2b835134002221906&q=" + cityName + "&days=1&aqi=yes&alerts=yes";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("lol", "onResponse: "+response.toString());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("thread1", "The thread inside api : "+Thread.currentThread().getName());



                            String city = response.getJSONObject("location").getString("name");
                            String cont = response.getJSONObject("location").getString("country");
                            String temp = response.getJSONObject("current").getString("temp_c");
                            String min = String .valueOf(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getDouble("mintemp_c"));String realFeel = String .valueOf(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getDouble("maxtemp_c"));

                            String sunRise = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro").getString("sunrise");
                            String moonRise =response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro").getString("moonrise");
                            String moonSet = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro").getString("moonset");
                            String sunSet = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro").getString("sunset");

                            String conditon = response.getJSONObject("current").getJSONObject("condition").getString("text");
                            int rain = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").getJSONObject(currDate).getInt("will_it_rain");




                            Message message = new Message();
                            Bundle bundle = new Bundle();

                            bundle.putString("conditon",conditon);
                            bundle.putInt("rain",rain);

                            bundle.putString("currRealFeel1",realFeel);
                            bundle.putString("currRealFeel2",min);
                            bundle.putString("stateName",city);
                            bundle.putString("country",cont);
                            bundle.putString("currTemp",temp);

                            bundle.putString("sunRise",sunRise);
                            bundle.putString("moonRise",moonRise);
                            bundle.putString("moonSet",moonSet);
                            bundle.putString("sunSet",sunSet);
                            message.setData(bundle);
                            handler.sendMessage(message);

                            String time2;
                            for(int i = 0;i<(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").getJSONObject(i)).length();i++){
                                if(i>12){
                                    time2 = (i-12)+":00 PM";
                                }
                                else{
                                    time2 = i+":00 AM";
                                }
                                String tempc = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").getJSONObject(i).getDouble("temp_c") + " °C";
                                String windspeed = "Windspeed : "+response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").getJSONObject(i).getDouble("wind_kph")+ " kph";

                                Message message1 = new Message();
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("time2",time2);
                                bundle1.putString("tempc",tempc);
                                bundle1.putString("windspeed",windspeed);
                                message1.setData(bundle1);
                                handler1.sendMessage(message1);

                            }


















                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}