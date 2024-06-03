package com.mirea.kt.holidays;



public class HolidaysCountry  {

    private String name;
    private String date;

    public HolidaysCountry(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}