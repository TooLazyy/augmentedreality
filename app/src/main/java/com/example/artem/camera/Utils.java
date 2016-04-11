package com.example.artem.camera;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

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

/**
 * Created by Artem on 06.03.2016.
 */
public class Utils {

    public  static Dialog startLoadingDialog (Context context,String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(message);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        return  dialog;
    }

    public static double getDistance(double lat1,double lon1,double alt1,
                                     double lat2,double lon2,double alt2) {
        double rad = 6378137.0;
        double f = 1.0 / 298.257224;

        //перевод широты долготы высоты в xyz координаты
        double cosLat = Math.cos(lat1 * Math.PI / 180.0);
        double sinLat = Math.sin(lat1 * Math.PI / 180.0);
        double cosLon = Math.cos(lon1 * Math.PI / 180.0);
        double sinLon = Math.sin(lon1*Math.PI/180.0);
        double C = 1.0 / Math.sqrt(cosLat * cosLat + (1 - f) * (1 - f) * sinLat * sinLat);
        double S = (1.0 - f) * (1.0 - f) * C;
        double x = (rad * C + alt1) * cosLat * cosLon;
        double y = (rad * C + alt1) * cosLat * sinLon;
        double z = (rad * S + alt1) * sinLat;

        double cosLat1 = Math.cos(lat2 * Math.PI / 180.0);
        double sinLat1 = Math.sin(lat2 * Math.PI / 180.0);
        double cosLon1 = Math.cos(lon2 * Math.PI / 180.0);
        double sinLon1 = Math.sin(lon2*Math.PI/180.0);
        double C1 = 1.0 / Math.sqrt(cosLat1 * cosLat1 + (1 - f) * (1 - f) * sinLat1 * sinLat1);
        double S1 = (1.0 - f) * (1.0 - f) * C1;
        double x1 = (rad * C1 + alt2) * cosLat1 * cosLon1;
        double y1 = (rad * C1 + alt2) * cosLat1 * sinLon1;
        double z1 = (rad * S1 + alt2) * sinLat1;

        return Math.sqrt(Math.pow(x-x1, 2)+Math.pow(y-y1, 2)+Math.pow(z-z1, 2));
    }

    public static double getElevationFromGoogleMaps(double latitude, double longitude) {
        double result = Double.NaN;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String url = "http://maps.googleapis.com/maps/api/elevation/xml"
                + "?locations=" + String.valueOf(latitude)
                + "," + String.valueOf(longitude)
                + "&sensor=true";
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                int r = -1;
                StringBuffer respStr = new StringBuffer();
                while ((r = instream.read()) != -1)
                    respStr.append((char) r);
                String tagOpen = "<elevation>";
                String tagClose = "</elevation>";
                if (respStr.indexOf(tagOpen) != -1) {
                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
                    int end = respStr.indexOf(tagClose);
                    String value = respStr.substring(start, end);
                    result = (double)(Double.parseDouble(value)); // convert from meters to feet
                }
                instream.close();
            }
        } catch (ClientProtocolException e) {}
        catch (IOException e) {}

        return result;
    }
}
