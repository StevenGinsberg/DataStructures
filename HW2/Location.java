package com.jetbrains;

public interface Location {
    enum City { NY, LA, CHI, HOU, MIA }
    Location.City getLocation();
}