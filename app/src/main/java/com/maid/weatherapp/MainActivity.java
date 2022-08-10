package com.maid.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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
        recyclerView  = findViewById(R.id.recyclerView);
        constraintLayout = (ConstraintLayout) findViewById(R.id.con);






        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(MainActivity.this,Locale.getDefault());
                        try {
                            List<Address> adress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            lon = adress.get(0).getLongitude();
                            lat = adress.get(0).getLatitude();
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
        TextView onDate = findViewById(R.id.onDate);
        TextView stateName = findViewById(R.id.stateName);
        TextView currtemp = findViewById(R.id.currTemp);
        TextView currRealFeel = findViewById(R.id.realFeel);
        TextView sun = findViewById(R.id.sunrise);
        TextView moon = findViewById(R.id.moonrise);
        TextView sunS =findViewById(R.id.sunset2);
        TextView moonS = findViewById(R.id.moonset);
        TextView country= findViewById(R.id.country);
        Date date = new Date();
        int currDate = date.getHours();
        Log.d("lol", "CURRDATE" + currDate);
        String url = "https://api.weatherapi.com/v1/forecast.json?key=89ee4513fd284b2b835134002221906&q=" + cityName + "&days=1&aqi=yes&alerts=yes";
        JsonObjectRequest stringRequest = new  JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("lol", response.toString());
                    String loc;
                    String city = response.getJSONObject("location").getString("name");
                    String cont = response.getJSONObject("location").getString("country");
                    String temp = response.getJSONObject("current").getString("temp_c");
                    String realFeel = String .valueOf(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getDouble("maxtemp_c"));
                    ImageView icon = findViewById(R.id.icon);
                    String min = String .valueOf(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getDouble("mintemp_c"));
                    currRealFeel.setText(realFeel+"/"+min+"°C");
                    stateName.setText(city);
                    country.setText(cont);
                    currtemp.setText(temp+"°C");

                    String sunRise = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro").getString("sunrise");
                    String moonRise =response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro").getString("moonrise");
                    String moonSet = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro").getString("moonset");
                    String sunSet = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro").getString("sunset");
                    moon.setText(moonRise);
                    sun.setText(sunRise);
                    sunS.setText(sunSet);
                    moonS.setText(moonSet);

                    TextView condi = findViewById(R.id.condi);


                    String time2;
                    String conditon = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String onDate1 = String.valueOf(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").getJSONObject(currDate).getString("time"));
                    //Log.d("condi",conditon);
                    int rain = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").getJSONObject(currDate).getInt("will_it_rain");
                    //Log.d("condi",String.valueOf(rain));
                    onDate.setText(onDate1);
                    if(currDate >= 4 && currDate <= 8&&rain==0){
                        constraintLayout.setBackgroundResource(R.drawable.dawn);
                        if(conditon.equals("Partly cloudy")){
                            icon.setImageResource(R.drawable.partiallycloudedmor);
                            condi.setText("Partly cloudy");
                        }
                        else if(conditon.equals("Light rain shower")){
                            icon.setImageResource(R.drawable.lightrainshowermor);
                            condi.setText("Light rain shower");
                        }
                        else if(conditon.equals("Clear")){
                            icon.setImageResource(R.drawable.clearmor);
                            condi.setText("Clear");
                        }
                        else if(conditon.equals("Heavy rain")){
                            icon.setImageResource(R.drawable.heavyrain);
                            condi.setText("Heavy rain");
                        }
                        else if(conditon.equals("Moderate rain")){
                            icon.setImageResource(R.drawable.rain);
                            condi.setText("Moderate rain");
                        }
                    }
                    else if(currDate >= 4 && currDate <= 8&&rain!=0){
                        constraintLayout.setBackgroundResource(R.drawable.rain);
                    }
                    else if(currDate > 8 && currDate < 17 && rain !=0){
                        constraintLayout.setBackgroundResource(R.drawable.rain);
                    }
                    else if (currDate > 8 && currDate < 17 && rain ==0) {
                        constraintLayout.setBackgroundResource(R.drawable.sky);
                        if(conditon.equals("Partly cloudy")){
                            icon.setImageResource(R.drawable.partiallycloudedmor);
                            condi.setText("Partly cloudy");
                        }
                        else if(conditon.equals("Light rain shower")){
                            icon.setImageResource(R.drawable.lightrainshowermor);
                            condi.setText("Light rain shower");
                        }
                        else if(conditon.equals("Clear")||conditon.equals("Sunny")){
                            icon.setImageResource(R.drawable.clearmor);
                            condi.setText("Clear");
                        }
                        else if(conditon.equals("Heavy rain")){
                            icon.setImageResource(R.drawable.heavyrain);
                            condi.setText("Heavy rain");
                        }
                        else if(conditon.equals("Moderate rain")){
                            icon.setImageResource(R.drawable.rain);
                            condi.setText("Moderate rain");
                        }
                    }
                    else if(currDate>=17&&currDate<=19&&rain!=0){
                        constraintLayout.setBackgroundResource(R.drawable.duskrain);
                    }
                    else if(currDate>=17&&currDate<=19){
                        constraintLayout.setBackgroundResource(R.drawable.dusk1);
                        if(conditon.equals("Partly cloudy")){
                            icon.setImageResource(R.drawable.partiallycloudedmor);
                            condi.setText("Partly cloudy");
                        }
                        else if(conditon.equals("Light rain shower")){
                            icon.setImageResource(R.drawable.lightrainshowermor);
                            condi.setText("Light rain shower");
                        }
                        else if(conditon.equals("Clear")||conditon.equals("Sunny")){
                            icon.setImageResource(R.drawable.clearmor);
                            condi.setText("Clear");
                        }
                        else if(conditon.equals("Heavy rain")){
                            icon.setImageResource(R.drawable.heavyrain);
                            condi.setText("Heavy rain");
                        }
                        else if(conditon.equals("Moderate rain")){
                            icon.setImageResource(R.drawable.rain);
                            condi.setText("Moderate rain");
                        }
                    }
                    else if ( currDate > 19&& currDate<23 && rain!=0) {
                        constraintLayout.setBackgroundResource(R.drawable.nskyrain);
                    }
                    else if ( currDate > 19&& currDate<23 && rain==0) {
                        constraintLayout.setBackgroundResource(R.drawable.nsky);
                        if(conditon.equals("Partly cloudy")){
                            icon.setImageResource(R.drawable.partiallycloudednight);
                            condi.setText("Partly cloudy");
                        }
                        else if(conditon.equals("Light rain shower")){
                            icon.setImageResource(R.drawable.lightrainshower);
                            condi.setText("Light rain shower");
                        }
                        else if(conditon.equals("Clear")||conditon.equals("Sunny")){
                            icon.setImageResource(R.drawable.clearnight);
                            condi.setText("Clear");
                        }
                        else if(conditon.equals("Heavy rain")){
                            icon.setImageResource(R.drawable.heavyrain);
                            condi.setText("Heavy rain");
                        }
                        else if(conditon.equals("Moderate rain")){
                            icon.setImageResource(R.drawable.rain);
                            condi.setText("Moderate rain");
                        }

                    }
                    else if(currDate >=0 && currDate<4&& rain!=0){
                        constraintLayout.setBackgroundResource(R.drawable.nskyrain);
                        icon.setImageResource(R.drawable.rainy);
                    }
                    else if(currDate >=0 && currDate<4&& rain==0 && conditon.equals("Partly cloudy")){
                        constraintLayout.setBackgroundResource(R.drawable.nsky);
                        if(conditon.equals("Partly cloudy")){
                            icon.setImageResource(R.drawable.partiallycloudednight);
                            condi.setText("Partly cloudy");
                        }
                        else if(conditon.equals("Light rain shower")){
                            icon.setImageResource(R.drawable.lightrainshower);
                            condi.setText("Light rain shower");
                        }
                        else if(conditon.equals("Clear")||conditon.equals("Sunny")){
                            icon.setImageResource(R.drawable.clearnight);
                            condi.setText("Clear");
                        }
                        else if(conditon.equals("Heavy rain")){
                            icon.setImageResource(R.drawable.heavyrain);
                            condi.setText("Heavy rain");
                        }
                        else if(conditon.equals("Moderate rain")){
                            icon.setImageResource(R.drawable.rain);
                            condi.setText("Moderate rain");
                        }
                    }







                    for(int i = 0;i<(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").getJSONObject(i)).length();i++){
                        if(i>12){
                          time2 = String.valueOf(i-12)+":00 PM";
                        }
                        else{
                            time2 = String.valueOf(i)+":00 AM";
                        }

                        String tempc = String.valueOf(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").getJSONObject(i).getDouble("temp_c")) + " °C";
                        String windspeed = "Windspeed : "+String.valueOf(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").getJSONObject(i).getDouble("wind_kph"))+ " kph";
                        list.add(new model(time2,tempc,windspeed));
                        Log.d("lolol","The windspeed is  + "+ windspeed);
                        Log.d("lolol","The temp is  + "+ tempc);
                        Log.d("lolol","the time is : "+i);

                        adapter adapter = new adapter(list,MainActivity.this);
                        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        SpacingDecor spacingDecor = new SpacingDecor(1,1);
                        recyclerView.addItemDecoration(spacingDecor);
                        recyclerView.setAdapter(adapter);

                    }


                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("lol", "" + error);
            }
        });
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}


