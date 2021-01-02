/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartcare.models;

/**
 *
 * @author jitojar
 */
public class Location {
    private String id;
    private String name;
    private String type; //private or NHS
    private float lat; //latitute
    private float lng; //longitute

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public Location(String id, String name, String type, float lat, float lng) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
    }
    
    public Location(String id, String name, String type, String lat, String lng) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.lat = Float.parseFloat(lat);
        this.lng = Float.parseFloat(lng);
    }
    
    
}
