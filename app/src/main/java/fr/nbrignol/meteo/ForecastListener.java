package fr.nbrignol.meteo;

public interface ForecastListener {
    public void forecastReady(Forecast f);
    public void forecastError();
}
