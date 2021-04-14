package edu.eci.arsw.weather.services.impl;

import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import edu.eci.arsw.weather.cache.CacheServiceI;
import edu.eci.arsw.weather.model.*;
import edu.eci.arsw.weather.services.HttpConnectionServicesI;
import edu.eci.arsw.weather.services.WeatherStatServicesI;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Weather services.
 */
@Service
public class WeatherStatServices implements WeatherStatServicesI {
    /**
     * The Http connection service.
     */
    @Autowired
    HttpConnectionServicesI httpConnectionService;
    @Autowired
    CacheServiceI cacheService;

    @Override
    public City getStatsByCity(String city) throws UnirestException {
        if (cacheService.getCache(city) == null || System.currentTimeMillis() - cacheService.getTime(city) >= 300000) {
            City cityNoCache = addDetails(city);
            cacheService.saveCache(city, cityNoCache);
        }
        return cacheService.getCache(city).getO1();
    }

    private City addDetails(String city) throws UnirestException {
        JSONObject jsonObject = httpConnectionService.getStatsCity(city);
        Gson gson = new Gson();
        City city1 = new City();
        city1.setName(gson.fromJson(String.valueOf(jsonObject.get("name")), String.class));
        city1.setCoord(gson.fromJson(String.valueOf(jsonObject.getJSONObject("coord")), Location.class));
        city1.setWeather(gson.fromJson(String.valueOf(jsonObject.getJSONArray("weather").getJSONObject(0)), Weather.class));
        city1.setMain(gson.fromJson(String.valueOf(jsonObject.getJSONObject("main")), Stats.class));
        city1.setWind(gson.fromJson(String.valueOf(jsonObject.getJSONObject("wind")), Wind.class));
        city1.setClouds(gson.fromJson(String.valueOf(jsonObject.getJSONObject("clouds")), Clouds.class));
        city1.setVisibility(gson.fromJson(String.valueOf(jsonObject.get("visibility")), Long.class));
        return city1;
    }

}