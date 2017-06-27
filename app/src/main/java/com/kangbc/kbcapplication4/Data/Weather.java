package com.kangbc.kbcapplication4.Data;

/**
 * Created by mac on 2017. 6. 22..
 */

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