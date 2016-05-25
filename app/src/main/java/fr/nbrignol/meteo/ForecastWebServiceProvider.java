package fr.nbrignol.meteo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForecastWebServiceProvider implements ForecastProvider {

    protected ForecastListener listener;
    protected Activity activity;

    public ForecastWebServiceProvider(Activity activity){
        this.activity = activity;
    }

    @Override
    public void setForecastListener(ForecastListener listener) {
        this.listener = listener;
    }

    @Override
    public void requestForecast(final String cityName) {

        Thread t = new Thread( new Runnable() {
            @Override
            public void run() {
                process(cityName);
            }
        });
        t.start();

    }


    protected void process(String cityName){
        final Forecast forecast = new Forecast();
        forecast.city = cityName;

        URL url;
        HttpURLConnection urlConnection;

        try {
            url = new URL("htttp://api.worldweatheronline.com/premium/v1/weather.ashx?key=4206b2a5c9f94404a0d121540162305&q="+cityName+"&format=json&num_of_days=5");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();

            String content = makeString(in);


            forecast.temperature = extractData(content);

            urlConnection.disconnect();

             this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.forecastReady(forecast);
                }
            });


        } catch(Exception e){
            this.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listener.forecastError();
                }
            });
        }

    }


    protected String makeString(InputStream in){

        try {

            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader buffer = new BufferedReader(reader);

            StringBuilder builder = new StringBuilder();

            String line;
            boolean process = true;

            while ( (line = buffer.readLine()) != null ) {
                builder.append(line).append('\n');
            }

            return builder.toString();
        } catch (Exception e) {
            return "";
        }
    }

    protected int extractData(String content) throws JSONException {

        JSONObject racine = new JSONObject(content);
        JSONObject data = racine.getJSONObject("data"); //data
        JSONArray conditions = data.getJSONArray("current_condition"); //les elements de current_condition
        JSONObject firstCondition = conditions.getJSONObject(0); //le premier (et seul) element du tableau

        int temperature = firstCondition.getInt("temp_C"); //on a notre valeur!

        /*int temperature = json.getJSONObject("data")
                .getJSONArray("current_condition")
                .getJSONObject(0)
                .getInt("temp_C");*/


        return temperature;
    }
}
