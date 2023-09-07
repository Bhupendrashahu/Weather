package com.bhupendra.wheaterapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bhupendra.wheaterapp.databinding.ActivityMainBinding;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        SearchCity("Noida");

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String format = sdf.format(cal.getTime());
        Format f = new SimpleDateFormat("EEEE");
        String str = f.format(new Date());
        binding.day.setText(str);
        binding.year.setText(format);
        binding.seach.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    SearchCity(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void SearchCity(String delhi) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Get get = retrofit.create(Get.class);
        try {
            get.getWeatherData(delhi, "a310eb46e72322656731179432cee9d0").enqueue(new Callback<Model>() {
                @Override
                public void onResponse(Call<Model> call, Response<Model> response) {
                    binding.location.setText(response.body().getName());
                    List<Model.Weather> weather = response.body().getWeather();
                    for (Model.Weather data : weather) {
                        String condi = data.getMain();
                        binding.condition.setText(condi);
                        conditionsEt(condi);
                    }
                    double tem = response.body().getMain().getTemp();
                    double v = tem - 273.0;
                    binding.temp.setText(String.valueOf(v));
                    binding.Wspeed.setText(String.valueOf(response.body().getWind().getSpeed()));
//                double temMax = response.body().getMain().getTempMax();
//                double vMax = temMax - 273.0;
//                binding.maxTemp.setText(String.valueOf(temMax));

//                double temMin = response.body().getMain().getTempMin();
//                double vMin = temMin - 273.0;
//                binding.minTemp.setText(String.valueOf(vMin));
                    binding.humidity.setText(String.valueOf(response.body().getMain().getHumidity()));
                    binding.conditionR.setText(String.valueOf(response.body().getMain().getHumidity()));
                    binding.sunrise.setText("6:3");
                    binding.sunset.setText("7:3");
                    binding.sea.setText(String.valueOf(response.body().getMain().getSeaLevel()));
                    binding.conditionR.setText(String.valueOf(response.body().getMain().getPressure()));
                }


                @Override
                public void onFailure(Call<Model> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Note in database", Toast.LENGTH_SHORT).show();
        }

    }

    public void conditionsEt(String condi) {
        if (condi.equals("Clouds")) {
            binding.loti.setAnimation(R.raw.bcloud);
            binding.loti.playAnimation();
            binding.constraint.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.clode));
        } else if (condi.equals("Haze")) {
            binding.loti.setAnimation(R.raw.haz);
            binding.loti.playAnimation();
            binding.constraint.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.snow));
        } else if (condi.equals("Rain")) {
            binding.loti.setAnimation(R.raw.rainani);
            binding.loti.playAnimation();
            binding.constraint.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.rains));
        } else if (condi.equals("Snow")) {
            binding.constraint.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.snow));
        }
    }
}