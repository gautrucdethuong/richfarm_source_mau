package com.infowindtech.eguru.StudentPanel.Model;


import com.google.android.gms.maps.model.LatLng;

public class LatLngBean {
    private String Title="";
    private String Snippet="";
    private String Latitude="";
    private String  Longitude="";
    private String user_id;
    private LatLng latlong;

    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }
    public String getSnippet() {
        return Snippet;
    }
    public void setSnippet(String snippet) {
        Snippet = snippet;
    }
    public String getLatitude() {
        return Latitude;
    }
    public void setLatitude(String latitude) {
        Latitude = latitude;
    }
    public String getLongitude() {
        return Longitude;
    }
    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public LatLng getLatlong() {
        return latlong;
    }

    public void setLatlong(LatLng latlong) {
        this.latlong = latlong;
    }
}
