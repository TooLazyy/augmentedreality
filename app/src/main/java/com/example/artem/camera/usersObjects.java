package com.example.artem.camera;

import com.quickblox.customobjects.model.QBCustomObject;

/**
 * Created by Artem on 06.03.2016.
 */
public class usersObjects {

    private String o_name;
    private String description;
    private double latitude;
    private double longitude;
    private String urlList;
    private String yearList;
    private  String ID;

    public usersObjects(QBCustomObject customObject){

        o_name = parseField(Consts.LIKED_O_NAME,customObject);
        description = parseField(Consts.LIKED_DESCRIPTION,customObject);
        latitude = Double.parseDouble(parseField(Consts.LIKED_LATITUDE, customObject));
        longitude = Double.parseDouble(parseField(Consts.LIKED_LONGITUDE,customObject));
        urlList = parseField(Consts.LIKED_O_URL, customObject);
        yearList = parseField(Consts.LIKED_O_YEAR, customObject);
        ID = customObject.getCustomObjectId();
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
    public String getUrls() {return urlList;}
    public String getYears() {return yearList;}
    public String getID() {return ID;}
}
