package fr.nbrignol.meteo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ForecastListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ForecastProvider provider = new ForecastMockProvider();
        ForecastProvider provider = new ForecastWebServiceProvider(this);
        provider.setForecastListener(this);

        provider.requestForecast("Marseille");

    }

    protected void displayForecast(Forecast forecast) {

        TextView cityLabel = (TextView) findViewById(R.id.forecast_city);
        cityLabel.setText( forecast.city );

        TextView tempLabel = (TextView) findViewById(R.id.forecast_temperature);
        tempLabel.setText( String.format("%d", forecast.temperature) );

    }

    @Override
    public void forecastReady(Forecast f) {
        displayForecast(f);
    }

    @Override
    public void forecastError() {
        TextView cityLabel = (TextView) findViewById(R.id.forecast_city);
        cityLabel.setText( "ERREUR" );

    }
}
