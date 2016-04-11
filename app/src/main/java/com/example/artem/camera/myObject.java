package com.example.artem.camera;

import com.quickblox.customobjects.model.QBCustomObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class myObject {

    private String o_name;
    private String description;
    private double latitude;
    private double longitude;
    private double altitude;
    private String urlList;
    private String yearList;

    public myObject(QBCustomObject customObject){

        o_name = parseField(Consts.O_NAME,customObject);
        description = parseField(Consts.DESCRIPTION,customObject);
        latitude = Double.parseDouble(parseField(Consts.LATITUDE, customObject));
        longitude = Double.parseDouble(parseField(Consts.LONGITUDE,customObject));
        altitude = Double.parseDouble(parseField(Consts.ALTITUDE,customObject));
        urlList = parseField(Consts.O_URL, customObject);
        yearList = parseField(Consts.O_YEAR, customObject);

    }

    private String parseField(String field, QBCustomObject customObject){
        Object object = customObject.getFields().get(field);
        if (object!=null){
            return object.toString();

        }
        return null;
    }

    public String getO_name() {return o_name;}
    public String getDescription() {return description;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
    public double getAltitude() {return altitude;}
    public String getUrls() {return urlList;}
    public String getYears() {return yearList;}

}
