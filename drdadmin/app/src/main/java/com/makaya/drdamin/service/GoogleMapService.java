package com.makaya.drdamin.service;

import android.graphics.Color;

import com.makaya.drdamin.domain.MapClass;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xbudi on 20/11/2016.
 */

public class GoogleMapService {
    // MY EVENT HANDLER

    private OnSetRoutedListener onSetRoutedListener;
    public interface OnSetRoutedListener {
        public <T> void onRouted(boolean isDrawing);
    }

    public void setOnRoutedListener(OnSetRoutedListener listener) {
        onSetRoutedListener = listener;
    }

    // /MY EVENT HANDLER

    private ArrayList<LatLng> locations;
    private ArrayList<LatLng> points;
    private PolylineOptions lineOptions = null;
    private MapClass.RootObject mapClass;
    private ArrayList<Polyline> aPolyline=new ArrayList<>();
    private GoogleMap mMap;
    private boolean includeDriverPos;
    private ArrayList<Integer> distances=new ArrayList<>();
    private ArrayList<Integer> durations=new ArrayList<>();
    private int totalDistance=0;
    private int totalDuration=0;

    public GoogleMapService(GoogleMap mMap)
    {
        this.mMap=mMap;
    }

    public void drawRoute(ArrayList<LatLng> locations, boolean includeDriverPos)
    {
        this.locations=locations;
        this.includeDriverPos=includeDriverPos;

        StringBuilder sb;
        for(int i=0;i<aPolyline.size();i++)
        {
            aPolyline.get(i).remove();
        }

        LatLng src=locations.get(0);
        LatLng dst=locations.get(locations.size()-1);

        sb = new StringBuilder("http://maps.googleapis.com/maps/api/directions/json");
        sb.append("?origin="+src.latitude+","+src.longitude);
        sb.append("&destination="+dst.latitude+","+dst.longitude);
        sb.append("&sensor=false");
        sb.append("&units=metric");
        sb.append("&mode=drive");

        String waypoints="";
        for(int i=1;i<locations.size()-1;i++)
        {
            src=locations.get(i);
            if (i==1)
                waypoints="&waypoints=";
            waypoints+=src.latitude+","+src.longitude+ "|";
        }
        sb.append(waypoints);
        // Start downloading json data from Google Directions API
        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(sb, MapClass.RootObject.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                //directionResult=(DirectionsResult) data;
                mapClass=(MapClass.RootObject) data;
                drawRoute();
                if (onSetRoutedListener!=null)
                    onSetRoutedListener.onRouted(true);
            }

            @Override
            public <T> void onPostedError(Exception data) {

            }
        });

    }

    private void drawRoute()
    {
        points = new ArrayList<>();
        lineOptions = new PolylineOptions();
        LatLng position;
        List<MapClass.Leg> legs=mapClass.routes.get(0).legs;

        if (includeDriverPos)
        {
            for(MapClass.Step step: mapClass.routes.get(0).legs.get(0).steps)
            {
                String polyline = "";
                polyline = step.polyline.points;
                List<LatLng> list = decodePoly(polyline);
                /** Traversing all points */
                for(int l=0;l<list.size();l++){
                    position = new LatLng(list.get(l).latitude, list.get(l).longitude);
                    points.add(position);
                }

            }

            lineOptions.addAll(points);
            lineOptions.width(10);
            lineOptions.color(Color.GREEN);

            if(lineOptions != null) {
                aPolyline.add(mMap.addPolyline(lineOptions));
            }
            legs.remove(0);
        }

        distances=new ArrayList<>();
        durations=new ArrayList<>();
        totalDistance=0;
        totalDuration=0;

        points = new ArrayList<>();
        lineOptions = new PolylineOptions();
        for(MapClass.Route rt: mapClass.routes)
        {
            for(MapClass.Leg leg: legs) //rt.legs)
            {

                for(MapClass.Step step: leg.steps)
                {
                    distances.add(step.distance.value);
                    durations.add(step.duration.value);
                    totalDistance+=step.distance.value;
                    totalDuration+=step.duration.value;

                    String polyline = "";
                    polyline = step.polyline.points;
                    List<LatLng> list = decodePoly(polyline);
                    /** Traversing all points */
                    for(int l=0;l<list.size();l++){
                        position = new LatLng(list.get(l).latitude, list.get(l).longitude);
                        points.add(position);
                    }

                }
            }
        }

        lineOptions.addAll(points);
        lineOptions.width(10);
        lineOptions.color(Color.RED);

        if(lineOptions != null) {
            aPolyline.add(mMap.addPolyline(lineOptions));
        }
    }

    public void calcRoute(ArrayList<LatLng> locations)
    {
        this.locations=locations;

        StringBuilder sb;

        LatLng src=locations.get(0);
        LatLng dst=locations.get(locations.size()-1);

        sb = new StringBuilder("http://maps.googleapis.com/maps/api/directions/json");
        sb.append("?origin="+src.latitude+","+src.longitude);
        sb.append("&destination="+dst.latitude+","+dst.longitude);
        sb.append("&sensor=false");
        sb.append("&units=metric");
        sb.append("&mode=drive");

        String waypoints="";
        for(int i=1;i<locations.size()-1;i++)
        {
            src=locations.get(i);
            if (i==1)
                waypoints="&waypoints=";
            waypoints+=src.latitude+","+src.longitude+ "|";
        }
        sb.append(waypoints);
        // Start downloading json data from Google Directions API
        PostDataModelUrl posData = new PostDataModelUrl();
        posData.execute(sb, MapClass.RootObject.class);
        posData.setOnDataPostedListener(new PostDataModelUrl.OnSetDataPostedListener() {
            @Override
            public <T> void onDataPosted(T data) {
                //directionResult=(DirectionsResult) data;
                mapClass=(MapClass.RootObject) data;
                calcRoute();
                if (onSetRoutedListener!=null)
                    onSetRoutedListener.onRouted(false);
            }

            @Override
            public <T> void onPostedError(Exception data) {

            }
        });
    }

    private void calcRoute()
    {
        distances=new ArrayList<>();
        durations=new ArrayList<>();
        totalDistance=0;
        totalDuration=0;

        for(MapClass.Route rt: mapClass.routes)
        {
            for(MapClass.Leg leg: rt.legs)
            {
                for(MapClass.Step step: leg.steps)
                {
                    distances.add(step.distance.value);
                    durations.add(step.duration.value);
                    totalDistance+=step.distance.value;
                    totalDuration+=step.duration.value;
                }
            }
        }

    }


    public ArrayList<Integer> getDistances()
    {
        return distances;
    }
    public ArrayList<Integer> getDurations()
    {
        return durations;
    }
    public int getTotalDistance()
    {
        return totalDistance;
    }
    public int getTotalDuration()
    {
        return totalDuration;
    }

    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
