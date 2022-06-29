package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView cityW, temperatureW, weatherConditionW, humidityW, maxTemperatureW, minTemperatureW, pressureW, windW;
    private ImageView imageViewW;
    private Button fabs, search;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        cityW = findViewById(R.id.textViewCityWeather);
        temperatureW = findViewById(R.id.textViewTempWeather);
        weatherConditionW = findViewById(R.id.textViewWeatherConditionWeather);
        humidityW = findViewById(R.id.HumidityWeather);
        maxTemperatureW = findViewById(R.id.MaxTempWeather);
        minTemperatureW = findViewById(R.id.MinTempWeather);
        pressureW = findViewById(R.id.PressureWeather);
        windW = findViewById(R.id.WindWeather);
        imageViewW = findViewById(R.id.myimage);
        fabs = findViewById(R.id.button6);
        search = findViewById(R.id.search);
        editText = findViewById(R.id.editTextCity);

        fabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = editText.getText().toString();
                getWeatherData(cityName);

                editText.setText("");
            }
        });
    }
    public void getWeatherData(String name)
    {
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithCityName(name);
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                if(response.isSuccessful())
                {
                    cityW.setText(response.body().getName() + ", " + response.body().getSys().getCountry());
                    temperatureW.setText(response.body().getMain().getTemp() + " °C");
                    weatherConditionW.setText(response.body().getWeather().get(0).getDescription());
                    humidityW.setText(" : " + response.body().getMain().getHumidity() + "%");
                    maxTemperatureW.setText(" : " + response.body().getMain().getTempMax() + " °C");
                    minTemperatureW.setText(" : " + response.body().getMain().getTempMin() + " °C");
                    pressureW.setText(" : " + response.body().getMain().getPressure());
                    windW.setText(" : " + response.body().getWind().getSpeed());

                    String iconCode = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(imageViewW);
                }
                else
                {
                    Toast.makeText(WeatherActivity.this,"City not found, please try again.",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {

            }
        });
    }
}