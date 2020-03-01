package com.dayakar.mgitian.Data;

import androidx.annotation.Keep;
@Keep
public class Transport_Data {

    @Keep private String route_name;

    @Keep private String route_no,stops;

    public Transport_Data() {
    }

    public Transport_Data(String route_no, String route_name, String stops) {
        this.route_no = route_no;
        this.route_name = route_name;
        this.stops = stops;
    }


    public String getRoute_no() {
        return route_no;
    }

    public void setRoute_no(String route_no) {
        this.route_no = route_no;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public String getStops() {
        return stops;
    }

    public void setStops(String stops) {
        this.stops = stops;
    }
}
