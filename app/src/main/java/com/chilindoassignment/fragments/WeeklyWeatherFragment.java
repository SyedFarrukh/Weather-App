package com.chilindoassignment.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chilindoassignment.adapter.WeatherForecastAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.chilindoassignment.R;
import com.chilindoassignment.Util.Config;
import com.chilindoassignment.activities.LoginActivity;
import com.chilindoassignment.model.WeatherForecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;


public class WeeklyWeatherFragment extends Fragment {

   private WeatherForecastAdapter adapter;
     RecyclerView recyclerView;
    ArrayList<WeatherForecast> weatherForecasts = new ArrayList<>();
        RequestQueue queue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_weather, container, false);
        ButterKnife.bind(view, getActivity());
        queue = Volley.newRequestQueue(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.forecast_recycler_view);

        if (Config.isNetworkAvailable(getActivity())) {
            updateUI();
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

    private void updateUI() {

        final String WEEKLY_WEATHER="http://api.openweathermap.org/data/2.5/forecast?lat="+LoginActivity.lat+"&lon="+LoginActivity.lng+"&appid=ccdc0ec5caa98471e40192a1f82f8744";


        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Please wait...", "Fetching data from server...", false, false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new WeatherForecastAdapter(getActivity(), weatherForecasts);
        recyclerView.setAdapter(adapter);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WEEKLY_WEATHER,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray =  response.getJSONArray("list");
                            for(int i = 0 ; i< jsonArray.length() ; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String dateAndTime = jsonObject.getString("dt_txt");
                                JSONObject main = jsonObject.getJSONObject("main");
                                float temp_min = Float.parseFloat(main.getString("temp_min"));
                                float temp_max = Float.parseFloat(main.getString("temp_max"));
                                JSONArray jsonIconArray = jsonObject.getJSONArray("weather");
                                String icon = null;
                                for(int j = 0 ; j< jsonIconArray.length() ; j++){
                                    JSONObject jsonObjectIcon = jsonIconArray.getJSONObject(j);
                                    icon = jsonObjectIcon.getString("icon");
                                }
                                weatherForecasts.add(new WeatherForecast(dateAndTime,temp_min, temp_max, icon));
                            }
                            loading.dismiss();
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
