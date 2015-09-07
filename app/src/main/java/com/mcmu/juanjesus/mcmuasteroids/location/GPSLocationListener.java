package com.mcmu.juanjesus.mcmuasteroids.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPSLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {

        Log.d("onLocationChanged -> ",
                location.toString() + " LAT: " + location.getLatitude() + " LONG: " + location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

        Log.d("onStatusChanged -> ", provider);
    }

    @Override
    public void onProviderEnabled(String provider) {

        Log.d("onProviderEnabled -> ", provider);
    }

    @Override
    public void onProviderDisabled(String provider) {

        Log.d("onProviderDisabled -> ", provider);
    }
}
