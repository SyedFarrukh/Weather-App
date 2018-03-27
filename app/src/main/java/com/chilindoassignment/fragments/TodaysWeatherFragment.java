package com.chilindoassignment.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chilindoassignment.R;
import com.chilindoassignment.Util.Config;
import com.chilindoassignment.activities.LoginActivity;
import com.chilindoassignment.model.TodayWeather;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.vistrav.ask.Ask;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TodaysWeatherFragment extends Fragment {

    @BindView(R.id.cityName)
    TextView cityName;
    @BindView(R.id.dateTime)
    TextView dateTime;
    @BindView(R.id.weatherIcon)
    ImageView weatherIcon;
    @BindView(R.id.currentTemperature)
    TextView currentTemp;
    @BindView(R.id.details)
    TextView details;
    RequestQueue queue = null;

    public static String latitude;
    public static String longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todays_weather, container, false);
        ButterKnife.bind(this, view);

        Ask.on(getActivity())
                .forPermissions(Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_FINE_LOCATION)
                .withRationales("Location permission need for map to work properly",
                        "In order to save file you will need to grant storage permission") //optional
                .go();
        queue = Volley.newRequestQueue(getActivity());



        if (Config.isNetworkAvailable(getActivity())) {
            todayWeatherAPICall();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please check your internet conncetion and try again. Thank you!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        }

        return view;
    }

    private void todayWeatherAPICall() {


        final String TODAY_WEATHER = "http://api.openweathermap.org/data/2.5/weather?lat="+LoginActivity.lat+"&lon="+LoginActivity.lng+"&appid=ccdc0ec5caa98471e40192a1f82f8744";

        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Please wait...", "Fetching data from server...", false, false);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, TODAY_WEATHER,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Response  : " + response);
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        TodayWeather todayWeather = gson.fromJson(String.valueOf(response), TodayWeather.class);

                        String _cityName = todayWeather.getName() + "," + todayWeather.getSys().getCountry();
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());

                        String _detail = todayWeather.getWeather().get(0).getMain() + "\n" +
                                todayWeather.getWeather().get(0).getDescription() + "\n" +
                                "Humidity :" + todayWeather.getMain().getHumidity() +
                                "\n" + "Pressure :" + todayWeather.getMain().getPressure();

                        String temp = String.valueOf(todayWeather.getMain().getTemp());
                        String icon = todayWeather.getWeather().get(0).getIcon();
                        String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";

                        cityName.setText(_cityName);
                        dateTime.setText(formattedDate);
                        currentTemp.setText(temp + " F");
                        details.setText(_detail);
                        Picasso.with(getActivity()).load(iconUrl).into(weatherIcon);

                        //System.out.println("CityName  : " + cityName+"\n"+formattedDate+"\n"+temp+"\n"+detail+"\n"+iconUrl);
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                    }

                });
        queue.add(jsonObjectRequest);
    }


}
