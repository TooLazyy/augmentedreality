package com.example.artem.camera;

import com.example.artem.camera.myObject;
import com.quickblox.customobjects.model.QBCustomObject;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {

    private static DataHolder dataHolder;
    private List<myObject> objectList;

    public static synchronized DataHolder getDataHolder() {
        if (dataHolder == null) {
            dataHolder = new DataHolder();
        }
        return dataHolder;
    }

    public int getObjectListSize() {
        if (objectList == null) {
            objectList = new ArrayList<myObject>();
        }
        return objectList.size();
    }

    public void clear() {
        objectList.clear();
    }

    public void addObjectToList(QBCustomObject customObject) {
        if (objectList == null) {
            objectList = new ArrayList<myObject>();
        }
        objectList.add(new myObject(customObject));
    }

    public int size() {
        if (objectList != null) {
            return objectList.size();
        }
        return 0;
    }

    public String getName(int position) {
        return objectList.get(position).getO_name();
    }

    public String getDescription(int position) {
        return objectList.get(position).getDescription();
    }

    public double getLatitude(int position) {
        return objectList.get(position).getLatitude();
    }

    public double getLongitude(int position) {
        return objectList.get(position).getLongitude();
    }

    public double getAltitude(int position) {
        return objectList.get(position).getAltitude();
    }

    public String getUrls(int position) {
        return objectList.get(position).getUrls();
    }

    public String getYears(int position) {
        return objectList.get(position).getYears();
    }

}
