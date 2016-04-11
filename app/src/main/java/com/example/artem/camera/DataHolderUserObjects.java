package com.example.artem.camera;

import com.quickblox.customobjects.model.QBCustomObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 06.03.2016.
 */
public class DataHolderUserObjects {

    private static DataHolderUserObjects dataHolder;
    private List<usersObjects> objectList;

    public static synchronized DataHolderUserObjects getDataHolder() {
        if (dataHolder == null) {
            dataHolder = new DataHolderUserObjects();
        }
        return dataHolder;
    }

    public int getObjectListSize() {
        if (objectList == null) {
            objectList = new ArrayList<usersObjects>();
        }
        return objectList.size();
    }
    public void addObjectToList(QBCustomObject customObject) {
        if (objectList == null) {
            objectList = new ArrayList<usersObjects>();
        }
        objectList.add(new usersObjects(customObject));
    }

    public void clear() {
        objectList.clear();
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

    public String getUrls(int position) {
        return objectList.get(position).getUrls();
    }

    public String getYears(int position) {
        return objectList.get(position).getYears();
    }

    public String getID(int position) {
        return objectList.get(position).getID();
    }
}
