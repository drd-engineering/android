package com.makaya.drd;

/**
 * Created by xbudi on 22/09/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.makaya.drd.domain.PointLocation;
import com.makaya.drd.domain.XLatLng;
import com.makaya.drd.library.GPSTracker;
import com.makaya.drd.library.GoogleMapService;
import com.makaya.drd.library.PublicFunction;

import java.util.ArrayList;


/**
 * Created by xbudi on 19/09/2016.
 */
public class PopupLocation extends FragmentActivity implements
        AdapterView.OnItemClickListener, OnMapReadyCallback {
    /*
    // MY EVENT HANDLER
    private OnSetLocationListener onSetLocationListener;
    public interface OnSetLocationListener {
        public void onSetLocation(String address, double lat, double lng);
    }

    public void setOnSetLocationListener(OnSetLocationListener listener) {
        onSetLocationListener = listener;
    }
    // /MY EVENT HANDLER
    */

    SupportMapFragment mapFragment;
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    GPSTracker gpsTracker;
    //static final int GOOGLE_API_CLIENT_ID = 0;
    //Marker currentMarker=null;
    StringBuilder jsonResults = new StringBuilder();
    LatLng defaultLatLng=new LatLng(0,0);
    ArrayList<Polyline> aPolyline=new ArrayList<>();
    ArrayList<Marker> aMarker=new ArrayList<>();
    int totalDistance=0; // KM /1000
    int totalDuration=0; // minutes / 60
    String totalDistanceText="";
    String totalDurationText="";
    Activity activity;
    AutoCompleteTextView autoCompleteLocation;
    Handler timerHandler;
    Runnable timerRunnable;
    ProgressBar progressBar;
    boolean initGps=true;
    RelativeLayout mapLayout;
    GoogleMapService mapSvr;

    ImageView imgClear;
    XLatLng latlng=new XLatLng();
    MainApplication global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);
        activity=this;
        global=(MainApplication)getApplication();
        mapLayout=(RelativeLayout)findViewById(R.id.mapLayout);
        //mapLayout.setVisibility(View.INVISIBLE);
        imgClear=(ImageView)findViewById(R.id.imgClear);
        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteLocation.setText("");
            }
        });
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        PublicFunction.setStatusBarColor(activity);
        //MainApplication.isMapClosed=false;
        /*PointLocation point=(PointLocation)global.getLocationResult().getTag();
        if (point!=null) {
            latlng.Latitude = point.Latitude;
            latlng.Longitude = point.Longitude;
        }*/
        XLatLng ll=(XLatLng) getIntent().getSerializableExtra("LatLong");
        latlng.Latitude = ll.Latitude;
        latlng.Longitude = ll.Longitude;
        initButton();
/*
    }

    @Override
    protected void onStart()
    {
        super.onStart();
*/
        gpsTracker=new GPSTracker(activity);//this.getBaseContext());
        gpsTracker.setOnLocationChangedListener(new GPSTracker.OnSetLocationChangedListener() {
            @Override
            public <T> void onLocationChanged(Location location) {
                if (initGps)
                    setUpLocation();
                initGps=false;
            }
        });

        AutoCompleteLocation autoComplite = new AutoCompleteLocation(this, findViewById(R.id.autoCompleteLocation));
        autoComplite.setOnSelectedAddressListener(new AutoCompleteLocation.OnSelectedAddressListener() {
            @Override
            public void onSelectedAddress(String placeId) {
                PublicFunction.hideKeyboard(activity);
                changeLocation(placeId);
            }
        });
        autoCompleteLocation=(AutoCompleteTextView) findViewById(R.id.autoCompleteLocation);
        autoCompleteLocation.setBackgroundColor(Color.TRANSPARENT);
        autoCompleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this,R.integer.GOOGLE_API_CLIENT_ID, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //Toast.makeText(activity,"Map onConnectionFailed",Toast.LENGTH_LONG).show();
                    }
                })
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        //Toast.makeText(activity,"Map onConnected",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        //Toast.makeText(activity,"Map onConnectionSuspended",Toast.LENGTH_LONG).show();
                    }
                })
                .build();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onBackPressed() {
        try {
            gpsTracker.stopUsingGPS();
        }catch(SecurityException p){}

        //activity.finish();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            gpsTracker.getLocation();
        }
    }

    private void initButton()
    {
        Button btnClose=(Button)findViewById(R.id.btn_cancel);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    gpsTracker.stopUsingGPS();
                    activity.finish();
                }catch(SecurityException p){

                }
            }
        });
        Button btnOK=(Button)findViewById(R.id.btn_set);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView address=(TextView)findViewById(R.id.detailLocation);


                /*((EditText)MainApplication.viewLocation).setText(address.getText().toString());
                MainApplication.latLng.set(MainApplication.defPosLocation, defaultLatLng);*/

                //MainApplication.isMapClosed=true;
                try {
                    /*if (global.getLocationResult()==null) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("address", address.getText().toString());

                        returnIntent.putExtra("lat", defaultLatLng.latitude);
                        returnIntent.putExtra("lng", defaultLatLng.longitude);
                        setResult(Activity.RESULT_OK, returnIntent);
                    }else{*/
                        /*EditText result=global.getLocationResult();
                        result.setText(address.getText().toString());*/
                        PointLocation point=gpsTracker.getPointLocation(activity,defaultLatLng.latitude,defaultLatLng.longitude);
                        //result.setTag(point);
                    //}

                    gpsTracker.stopUsingGPS();
                    //activity.finish();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("Point", point);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }catch(SecurityException p){

                }
            }
        });

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

        //LatLng ll=MainApplication.latLng.get(MainApplication.defPosLocation);

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
        try {
            aMarker.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_markerredezgo))
                    //.anchor(0.0f, 0.0f)
                    .position(new LatLng(lat, lng))
                    .draggable(true)));
        }catch (Exception x)
        {
            return;
        }


        moveLocation(lat, lng);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                //Log.d(TAG, "latitude : "+ marker.getPosition().latitude);
                marker.setSnippet(marker.getPosition().latitude+"");
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

                setTextDescription(gpsTracker.getAddress(mGoogleApiClient.getContext(),marker.getPosition().latitude,marker.getPosition().longitude));

                defaultLatLng=new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);

                //MainApplication.latLng.set(MainApplication.defPosLocation, defaultLatLng);

                //drawRoute();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

        });
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(view.getContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void moveLocation(double lat, double lng)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 16));

        //aMarker.get(MainApplication.defPosLocation).setPosition(new LatLng(lat, lng));
        aMarker.get(0).setPosition(new LatLng(lat, lng));

        defaultLatLng=new LatLng(lat,lng);
        //MainApplication.latLng.set(MainApplication.defPosLocation, defaultLatLng);

        //drawRoute();
        setTextDescription(gpsTracker.getAddress(mGoogleApiClient.getContext(),lat,lng));
    }

    /*private void drawRoute()
    {
        mapSvr.drawRoute(MainApplication.latLng, false);
        mapSvr.setOnRoutedListener(new GoogleMapService.OnSetRoutedListener() {
            @Override
            public <T> void onRouted(boolean isDrawing) {
                MainApplication.distances=(ArrayList<Integer>) mapSvr.getDistances().clone();
                MainApplication.durations=(ArrayList<Integer>) mapSvr.getDurations().clone();
                setTextDuration(mapSvr.getTotalDistance(), mapSvr.getTotalDuration());
            }
        });

    }*/

    private void changeLocation(String placeId)
    {

        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            Place myPlace = places.get(0);
                            LatLng queriedLocation = myPlace.getLatLng();

                            //Log.v("Latitude is", "" + queriedLocation.latitude);
                            //Log.v("Longitude is", "" + queriedLocation.longitude);

                            moveLocation(queriedLocation.latitude, queriedLocation.longitude);

                            //setTextDescription(myPlace.getAddress());
                        }
                        places.release();
                    }
                });
    }

    void setTextDescription(CharSequence addr)
    {

        TextView faddr = (TextView)findViewById(R.id.detailLocation);
        faddr.setText(addr);


    }

    /*void setTextDuration(int totalDistance, int totalDuration)
    {
        String str="Distance: " +MainApplication.toKM(totalDistance)+" | Duration: "+MainApplication.toDuration(totalDuration);
        MainApplication.distanceDuration=str;
        TextView dd = (TextView)findViewById(R.id.durationDistance);
        if (MainApplication.defaultTabPos!=MainApplication.enumSearchType.POINT2POINT.ordinal()) {
            dd.setVisibility(View.GONE);
        }else {
            dd.setVisibility(View.VISIBLE);
            dd.setText(str);

        }

    }*/


}