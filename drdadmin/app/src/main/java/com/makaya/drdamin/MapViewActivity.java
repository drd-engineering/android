package com.makaya.drdamin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makaya.drdamin.domain.XLatLng;
import com.makaya.drdamin.service.GPSTracker;
import com.makaya.drdamin.service.GoogleMapService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by xbudi on 01/03/2017.
 */

public class MapViewActivity extends FragmentActivity implements
        OnMapReadyCallback {

    SupportMapFragment mapFragment;
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    GPSTracker gpsTracker;
    static final int GOOGLE_API_CLIENT_ID = 0;
    ArrayList<Marker> aMarker=new ArrayList<>();
    Activity activity;
    ProgressBar progressBar;
    boolean initGps=true;
    RelativeLayout mapLayout;
    GoogleMapService mapSvr;

    XLatLng latlng=new XLatLng();
    MainApplication global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view_activity);
        activity=this;
        mapLayout=(RelativeLayout)findViewById(R.id.mapLayout);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        Button btnClose=(Button)findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        latlng.Latitude=getIntent().getDoubleExtra("lat",0);
        latlng.Longitude=getIntent().getDoubleExtra("lng",0);

        gpsTracker=new GPSTracker(activity);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapSvr=new GoogleMapService(mMap);
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        try {
            googleMap.setMyLocationEnabled(true);
        }catch(SecurityException p){}

        googleMap.setTrafficEnabled(false);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        setUpLocation();
    }

    void setUpLocation()
    {
        mapLayout.setVisibility(View.VISIBLE);

        double lat, lng;
        if (latlng.Latitude!=0){
            lat = latlng.Latitude;
            lng = latlng.Longitude;
        }else {
            lat = gpsTracker.getLatitude();
            lng = gpsTracker.getLongitude();
        }

        if (lat!=0) {
            initGps=false;
            progressBar.setVisibility(View.GONE);
            setUpMarking(lat, lng);
        }

    }

    void setUpMarking(double lat, double lng)
    {
        aMarker.add(mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker2))
                .position(new LatLng(lat, lng))));

        moveLocation(lat, lng);
    }

    public void moveLocation(double lat, double lng)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 16));

        aMarker.get(0).setPosition(new LatLng(lat, lng));

        setTextDescription(lat,lng);
    }

    void setTextDescription(double lat, double lng)
    {
        String addr=gpsTracker.getAddress(mGoogleApiClient.getContext(),lat,lng).toString();
        addr+="\n Lat: "+lat+", Lng: "+lng;
        TextView faddr = (TextView)findViewById(R.id.detailLocation);
        faddr.setText(addr);
    }
}
