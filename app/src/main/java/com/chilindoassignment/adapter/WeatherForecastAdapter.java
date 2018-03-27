package com.chilindoassignment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chilindoassignment.R;
import com.chilindoassignment.model.WeatherForecast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import static java.lang.String.format;


public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<WeatherForecast> weather;

    public WeatherForecastAdapter(Context context,ArrayList<WeatherForecast> weather) {
        this.mContext = context;
        this.weather = weather;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weekly_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String temperatureMin = mContext.getString(R.string.temperature_with_degree,
                format(Locale.getDefault(), "%.0f",
                        weather.get(position).getTemperatureMin()));
        String temperatureMax = mContext.getString(R.string.temperature_with_degree,
                format(Locale.getDefault(), "%.0f",
                        weather.get(position).getTemperatureMax()));

        holder.mDateTime.setText(weather.get(position).getDateTime());

        String img = "http://openweathermap.org/img/w/" + weather.get(position).getIcon() + ".png";
        Picasso.with(mContext).load(img).into(holder.mIcon);
        if (weather.get(position).getTemperatureMin() > 0) {
            temperatureMin = "+" + temperatureMin;
        }
        holder.mTemperatureMin.setText(temperatureMin);
        if (weather.get(position).getTemperatureMax() > 0) {
            temperatureMax = "+" + temperatureMax;
        }
        holder.mTemperatureMax.setText(temperatureMax);

    }

    @Override
    public int getItemCount() {
         return weather.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder {

        private TextView mDateTime;
        private ImageView mIcon;
        private TextView mTemperatureMin;
        private TextView mTemperatureMax;

        public MyViewHolder(View itemView) {
            super(itemView);

            mDateTime = (TextView) itemView.findViewById(R.id.forecast_date_time);
            mIcon = (ImageView) itemView.findViewById(R.id.forecast_icon);
            mTemperatureMin = (TextView) itemView.findViewById(R.id.forecast_temperature_min);
            mTemperatureMax = (TextView) itemView.findViewById(R.id.forecast_temperature_max);
        }
    }
}

