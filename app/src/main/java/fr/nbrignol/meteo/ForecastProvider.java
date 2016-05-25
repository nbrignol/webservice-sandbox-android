package fr.nbrignol.meteo;

public interface ForecastProvider {

    public void setForecastListener(ForecastListener listener);
    public void requestForecast(String cityName);

}
