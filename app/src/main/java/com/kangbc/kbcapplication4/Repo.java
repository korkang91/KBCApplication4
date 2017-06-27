package com.kangbc.kbcapplication4;

import com.kangbc.kbcapplication4.Data.Coord;
import com.kangbc.kbcapplication4.Data.Main;
import com.kangbc.kbcapplication4.Data.Weather;
import com.kangbc.kbcapplication4.Data.Wind;

import java.util.List;

/**
 * Created by mac on 2017. 6. 22..
 */

public class Repo {
    Main main;
    Wind wind;
    Coord coord;
    public String base;
    public List<Weather> weather;

    public List<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Coord getCoord() {
        return coord;
    }

    public Wind getWind() {
        return wind;
    }

    public Main getMain() {
        return main;
    }

}