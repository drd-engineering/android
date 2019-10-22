package com.makaya.drd.domain;

import java.util.List;

public class MapClass {

    public class GeocodedWaypoint
    {
        public String geocoder_status;
        public String place_id;
        public List<String> types;
    }

    public class Northeast
    {
        public double lat;
        public double lng;
    }

    public class Southwest
    {
        public double lat;
        public double lng;
    }

    public class Bounds
    {
        public Northeast northeast;
        public Southwest southwest;
    }

    public class Distance
    {
        public String text;
        public int value;
    }

    public class Duration
    {
        public String text;
        public int value;
    }

    public class EndLocation
    {
        public double lat;
        public double lng;
    }

    public class StartLocation
    {
        public double lat;
        public double lng;
    }

    public class Distance2
    {
        public String text;
        public int value;
    }

    public class Duration2
    {
        public String text;
        public int value;
    }

    public class EndLocation2
    {
        public double lat;
        public double lng;
    }

    public class Polyline
    {
        public String points;
    }

    public class StartLocation2
    {
        public double lat;
        public double lng;
    }

    public class Step
    {
        public Distance2 distance;
        public Duration2 duration;
        public EndLocation2 end_location;
        public String html_instructions;
        public Polyline polyline;
        public StartLocation2 start_location;
        public String travel_mode;
        public String maneuver;
    }

    public class Leg
    {
        public Distance distance;
        public Duration duration;
        public String end_address;
        public EndLocation end_location;
        public String start_address;
        public StartLocation start_location;
        public List<Step> steps;
        public List<Object> traffic_speed_entry;
        public List<Object> via_waypoint;
    }

    public class OverviewPolyline
    {
        public String points;
    }

    public class Route
    {
        public Bounds bounds;
        public String copyrights;
        public List<Leg> legs;
        public OverviewPolyline overview_polyline;
        public String summary;
        public List<Object> warnings;
        public List<Integer> waypoint_order;
    }

    public class RootObject
    {
        public List<GeocodedWaypoint> geocoded_waypoints;
        public List<Route> routes;
        public String status;
    }


}