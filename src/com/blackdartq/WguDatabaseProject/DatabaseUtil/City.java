package com.blackdartq.WguDatabaseProject.DatabaseUtil;

public class City{
    public City(int cityId, String cityName, int countryId) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.countryId = countryId;
    }

    public int cityId;
    public String cityName;
    public int countryId;
}
