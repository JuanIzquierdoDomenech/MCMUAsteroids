package com.mcmu.juanjesus.mcmuasteroids.activities;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mcmu.juanjesus.mcmuasteroids.R;
import com.mcmu.juanjesus.mcmuasteroids.views.GameView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShowLocationInfoActivity extends AppCompatActivity implements LocationListener{

    //region Private Member Variables

    @Bind(R.id.txtv_location_info) TextView textViewShowInfo;

    private static final long MIN_TIME = 10 * 1000; // 10 seconds
    private static final long MIN_DISTANCE = 5; // meters
    private static final String[] A = {"n/d", "precise", "imprecise"};
    private static final String[] P = {"n/d", "low", "medium", "high"};
    private static final String[] E = {"out of service", "temporarily not available", "available"};
    private LocationManager locManager;
    private String provider;

    //endregion


    //region Activity lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_location_info);

        // Inject butter knife dependencies
        ButterKnife.bind(this);

        locManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        log("Localization providers: \n");
        showProviders();

        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        criteria.setAltitudeRequired(false);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        provider = locManager.getBestProvider(criteria, true);
        log("Best provider: " + provider + "\n");
        log("Beggining last known location:");
        Location location = locManager.getLastKnownLocation(provider);
        showLocation(location);
    }

    @Override
    protected void onResume() {

        super.onResume();
        locManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, this);
    }

    @Override
    protected void onPause() {

        super.onPause();
        locManager.removeUpdates(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    //endregion


    //region LocationListener
    @Override
    public void onLocationChanged(Location location) {
        log("New location: ");
        showLocation(location);
    }

    @Override
    public void onProviderDisabled(String provider) {
        log("Provider disabled: " + provider + "\n");
    }

    @Override
    public void onProviderEnabled(String provider) {
        log("Provider enabled: " + provider + "\n");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        log("Provider state changed: " + provider + ", status=" + E[Math.max(0, status)] + ", extras=" + extras + "\n");
    }

    //endregion

    private void showProviders() {
        log("Location providers: \n");
        List<String> providers = locManager.getAllProviders();
        for(String provider : providers) {
            showProvider(provider);
        }
    }

    private void showProvider(String provider) {
        LocationProvider info = locManager.getProvider(provider);
        log("LocationProvider[" + "getName=" + info.getName()
        + ", isProviderEnabled=" + locManager.isProviderEnabled(provider)
        + ", getAccuracy=" + A[Math.max(0, info.getAccuracy())]
        + ", getPowerRequirement=" + P[Math.max(0, info.getPowerRequirement())]
        + ", hasMonetaryCost=" + info.hasMonetaryCost()
        + ", requiresCell=" + info.requiresCell()
        + ", requiresNetwork=" + info.requiresNetwork()
        + ", requiresSatellite=" + info.requiresSatellite()
        + ", supportsAltitude=" + info.supportsAltitude()
        + ", supportsBearing=" + info.supportsBearing()
        + ", supportsSpeed=" + info.supportsSpeed()
        + " ]\n");
    }

    private void showLocation(Location location) {
        if (location == null) {
            log("Unknown location\n");
        } else {
            log(location.toString() + "\n");
        }
    }

    private void log(String s) {
        textViewShowInfo.append(s + "\n");
    }
}
