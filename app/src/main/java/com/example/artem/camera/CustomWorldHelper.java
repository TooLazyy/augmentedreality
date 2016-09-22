package com.example.artem.camera;

import android.annotation.SuppressLint;
import android.content.Context;

import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.example.artem.camera.GPS.FallbackLocationTracker;
import com.example.artem.camera.GPS.ProviderLocationTracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("SdCardPath")
public class CustomWorldHelper {
    public static World sharedWorld;
    public static ArrayList<Map<String, Object>> usedObjects;
    static Map<String, Object> m;
    private static double latitude=0;
    private static double longitude=0;
    private static double altitude=0;

    public static World generateObjects(Context context, ArrayList<Map<String, Object>> objArray) {
        if (sharedWorld != null) {
            return sharedWorld;
        }

        sharedWorld = new World(context);
        //public static World generateObjects(Context context,ArrayList<Map<String,Object>> geoObjects)
        // The user can set the default bitmap. This is useful if you are
        // loading images form Internet and the connection get lost
        sharedWorld.setDefaultImage(R.drawable.holder);

        ArrayList<Map<String, Object>> objects = objArray;
        latitude = 48.806813;
        longitude = 44.744190;
        altitude = 18.0;
        usedObjects = new ArrayList<Map<String, Object>>();
        sharedWorld.setGeoPosition(latitude, longitude);
        double distanceToObject = (double) MainActivity.seekRadius;

        if (objects.size() > 0) {
            for (int i = 0; i < objects.size(); i++) {
                if (latitude != 0 && longitude != 0) {
                    String lat2 = objects.get(i).get("lat").toString();
                    String lon2 = objects.get(i).get("long").toString();
                    String alt2 = objects.get(i).get("alt").toString();
                    double a = Double.parseDouble(lat2);
                    double b = Double.parseDouble(lon2);
                    double c = Double.parseDouble(alt2);
                    double dist = Utils.getDistance(latitude,longitude,altitude,a,b,c);
                    if (dist <= distanceToObject) {
                        m = new HashMap<String, Object>();

                        GeoObject gObject = new GeoObject(i);
                        gObject.setGeoPosition(a, b);
                        Map<String,Object> linkYear = new HashMap();
                        linkYear = (Map)objects.get(i).get("urls");
                        Map.Entry<String,Object> entry = linkYear.entrySet().iterator().next();
                        gObject.setImageUri(entry.getValue().toString());
                        gObject.setName(objects.get(i).get("name").toString());
                        gObject.setDistanceFromUser(dist);

                        m.put("name", objects.get(i).get("name").toString());
                        m.put("desc", objects.get(i).get("desc").toString());
                        m.put("lat", objects.get(i).get("lat").toString());
                        m.put("long", objects.get(i).get("long").toString());
                        m.put("dist", String.valueOf(Math.abs(dist)));
                        m.put("urls", linkYear);
                        usedObjects.add(m);
                        sharedWorld.addBeyondarObject(gObject);
                    }
                }
            }
        } else {
            Toast.makeText(context, "Объектов поблизости не найдено.\nПопробуйт ещё раз.", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(context, usedObjects.size()+"", Toast.LENGTH_LONG).show();
        return sharedWorld;
    }

    //рассчет расстояния между 2 точками
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return (dist);
    }

    //радианы в градусы
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    //градусы в радианы
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }




}
