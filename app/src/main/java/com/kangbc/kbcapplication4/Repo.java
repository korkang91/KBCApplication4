package com.kangbc.kbcapplication4;

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

    public class Main {
        Double temp;
        Integer pressure;
        Integer humidity;
        Double temp_min;
        Double temp_max;


        public Double getTemp() {
            return temp;
        }

        public Integer getPressure() {
            return pressure;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public Double getTemp_min() {
            return temp_min;
        }

        public Double getTemp_max() {
            return temp_max;
        }
    }

    public class Coord {
        public double lat;
        public double lon;

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }
    }

    public class Wind {
        public Double speed;
        public Double deg;

        public Double getSpeed() {
            return speed;
        }

        public Double getDeg() {
            return deg;
        }
    }

    public class Weather {
        private String id;
        private String main;
        private String description;
        private String icon;

        public String getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getIcon() {
            return icon;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

}