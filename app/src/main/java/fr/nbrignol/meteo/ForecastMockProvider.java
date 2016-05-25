package fr.nbrignol.meteo;

public class ForecastMockProvider implements ForecastProvider {

    protected ForecastListener listener;

    @Override
    public void setForecastListener(ForecastListener listener) {
        this.listener = listener;
    }

    @Override
    public void requestForecast(String cityName) {

        Forecast forecast = new Forecast();
        forecast.city = cityName;
        forecast.temperature = 12;
        forecast.windSpeed = 50;
        forecast.label = "Nuageux";

        this.listener.forecastReady(forecast);
    }
}
