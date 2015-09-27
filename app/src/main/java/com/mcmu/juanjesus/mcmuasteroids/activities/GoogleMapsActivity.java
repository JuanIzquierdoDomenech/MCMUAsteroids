package com.mcmu.juanjesus.mcmuasteroids.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mcmu.juanjesus.mcmuasteroids.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoogleMapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener, OnMapReadyCallback {

    //region Private member variables
    @Bind(R.id.btn_gm_go_upv) Button btnGoUpv;
    @Bind(R.id.btn_gm_go_my_location) Button btnGoMyLocation;
    @Bind(R.id.btn_gm_add_marker) Button btnAddMarker;

    private final LatLng UPV = new LatLng(39.481106, -0.340987);
    private MapFragment mapFragment;
    //endregion

    //region Activity lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        // Inject butter knife dependencies
        ButterKnife.bind(this);

        mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    //endregion


    //region GoogleMap.OnMapClickListener
    @Override
    public void onMapClick(LatLng latLng) {
        mapFragment.getMap().addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
    }
    //endregion


    //region OnMapReadyCallback

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV, 15));
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.addMarker(new MarkerOptions()
                .position(UPV)
                .title("UPV")
                .snippet("Universidad Politécnica de Valencia")
                .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_compass))
                .anchor(0.5f, 0.5f));
        googleMap.setOnMapClickListener(this);
    }

    //endregion


    //region UI binding
    @OnClick(R.id.btn_gm_go_upv)
    protected void goToUpv() {
        mapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLng(UPV));
    }

    @OnClick(R.id.btn_gm_go_my_location)
    protected void goToMyLocation() {
        if(mapFragment.getMap().getMyLocation() != null) {
            mapFragment.getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mapFragment.getMap().getMyLocation().getLatitude()
                            , mapFragment.getMap().getMyLocation().getLongitude()), 15));
        }
    }

    @OnClick(R.id.btn_gm_add_marker)
    protected void addMarker() {
        mapFragment.getMap().addMarker(new MarkerOptions().
                position(mapFragment.getMap().getCameraPosition().target));
    }
    //endregion
}
