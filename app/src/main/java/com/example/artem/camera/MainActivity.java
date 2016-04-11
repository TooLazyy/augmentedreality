package com.example.artem.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.example.artem.camera.GPS.FallbackLocationTracker;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.QBSettings;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    Map<String, Object> m;
    public static int seekRadius = 0;
    public static ArrayList<Map<String, Object>> tempest;
    private SeekBar seekBar;
    private TextView textView;
    private Dialog progressDialog;
    private Spinner sFrom;
    private Spinner sTo;
    public Integer yearFrom;
    public Integer yearTo;
    private QBUser user;
    private QBSession qSession;
    public static double latitude = 0;
    public static double longitude = 0;
    public static double altitude = 0;

    private void testSession() {
        if (user == null) {
            user = new QBUser(Consts.USER_LOGIN, Consts.USER_PASSWORD);
        }
        QBAuth.createSession(user, new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                qSession = qbSession;
                getObjectList();
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(List<String> list) {
                Toast.makeText(getBaseContext(), list.get(0), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getObjectList() {
        QBCustomObjects.getObjects(Consts.CLASS_NAME, new QBEntityCallbackImpl<ArrayList<QBCustomObject>>() {
            @Override
            public void onSuccess(ArrayList<QBCustomObject> qbCustomObjects, Bundle bundle) {

                if (DataHolder.getDataHolder().size() > 0) {
                    DataHolder.getDataHolder().clear();
                }

                if (qbCustomObjects != null && qbCustomObjects.size() != 0) {
                    for (QBCustomObject customObject : qbCustomObjects) {
                        DataHolder.getDataHolder().addObjectToList(customObject);
                    }
                }
                startDisplayNoteListActivity();
            }

            @Override
            public void onError(List<String> strings) {
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), strings.get(0), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static FallbackLocationTracker gps;

    private void startDisplayNoteListActivity() {
        if (gps != null) {
            if (gps.hasLocation()) {
                latitude = gps.getLocation().getLatitude();
                longitude = gps.getLocation().getLongitude();
            } else if (gps.hasPossiblyStaleLocation()) {
                latitude = gps.getPossiblyStaleLocation().getLatitude();
                longitude = gps.getPossiblyStaleLocation().getLongitude();
            }
        }
        new getAltitude().execute();
    }

    public void showLertSettings() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        alert.setTitle("GPS настройки");

        alert.setMessage("GPS выключен");
        alert.setPositiveButton("Настройки", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                MainActivity.this.startActivity(intent);
            }
        });

        alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        QBSettings.getInstance().fastConfigInit(String.valueOf(Consts.APP_ID), Consts.AUTH_KEY, Consts.AUTH_SECRET);
        gps = new FallbackLocationTracker(MainActivity.this);
        gps.start();
        if (gps != null) {
            if (gps.hasLocation()) {
                latitude = gps.getLocation().getLatitude();
                longitude = gps.getLocation().getLongitude();
            } else if (gps.hasPossiblyStaleLocation()) {
                latitude = gps.getPossiblyStaleLocation().getLatitude();
                longitude = gps.getPossiblyStaleLocation().getLongitude();
            }
        }

        user = LoginActivity.getUser();
        final Button myButton = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView2);
        seekBar = (SeekBar) findViewById(R.id.seekBar2);
        textView.setText("Радиус: " + seekBar.getProgress() + " метров");
        progressDialog = Utils.startLoadingDialog(MainActivity.this, "Подождите, идет загрузка!");
        sFrom = (Spinner) findViewById(R.id.sFrom);
        sTo = (Spinner) findViewById(R.id.sTo);
        Integer data[] = new Integer[121];
        for (int i = 0; i < 121; i++) {
            data[i] = 1896 + i;
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                this,
                R.layout.spinner_item_layout,
                data
        );
        sFrom.setAdapter(adapter);
        sTo.setAdapter(adapter);
        sTo.setSelection(data.length - 1);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekRadius = seekBar.getProgress();
                yearFrom = Integer.valueOf(sFrom.getSelectedItem().toString());
                yearTo = Integer.valueOf(sTo.getSelectedItem().toString());
                progressDialog.show();
                if (qSession == null) {
                    testSession();
                } else {
                    getObjectList();
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText("Радиус: " + progress + " метров");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private ArrayList<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> returnData = new ArrayList<Map<String, Object>>();
        if (DataHolder.getDataHolder().size() > 0) {
            for (int i = 0; i < DataHolder.getDataHolder().size(); i++) {
                m = new HashMap<String, Object>();
                m.put(Consts.ATTRIBUTE_NAME, DataHolder.getDataHolder().getName(i));
                m.put(Consts.ATTRIBUTE_DESCRIPTION, DataHolder.getDataHolder().getDescription(i));
                m.put(Consts.ATTRIBUTE_LATITUDE, DataHolder.getDataHolder().getLatitude(i));
                m.put(Consts.ATTRIBUTE_ALTITUDE, DataHolder.getDataHolder().getAltitude(i));
                m.put(Consts.ATTRIBUTE_LONGITUDE, DataHolder.getDataHolder().getLongitude(i));
                String urlArray[] = DataHolder.getDataHolder()
                        .getUrls(i)
                        .replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        .replace("\\", "")
                        .replace(" ", "")
                        .split(",");
                String yearArray[] = DataHolder.getDataHolder()
                        .getYears(i)
                        .replace("[", "")
                        .replace("]", "")
                        .replace(" ", "")
                        .split(",");
                Map yearUrl = new HashMap();
                for (int j = urlArray.length - 1; j >= 0; j--) {
                    int x = Integer.valueOf(yearArray[j]);
                    if (isInInterval(x)) {
                        yearUrl.put(yearArray[j], urlArray[j]);
                    }
                }
                if (yearUrl.size() > 0) {
                    m.put(Consts.ATTRIBUTE_URLS, yearUrl);
                    returnData.add(m);
                }
            }

        }
        return returnData;
    }

    @Override
    protected void onResume() {
        super.onResume();
        CustomWorldHelper.sharedWorld = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            System.exit(0);
        }
        if (id == R.id.action_places) {
            if (user != null) {
                if (user.getLogin() != "User1") {
                    //Toast.makeText(MainActivity.this, "Избранное", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, LikedObjectsActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "Для просмотра списка избранных объектов,\nвыполните вход в систему", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Для просмотра списка избранных объектов,\nвыполните вход в систему", Toast.LENGTH_SHORT).show();
            }
        }

        if (id == R.id.action_auth) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isInInterval(Integer year) {
        return year >= yearFrom && year <= yearTo;
    }

    public void showLertSettings(final Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Настройки GPS");

        alert.setMessage("GPS выключен");
        alert.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                context.startActivity(intent);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    public static FallbackLocationTracker getGps() {
        if (gps != null) {
            return gps;
        } else {
            return null;
        }
    }

    private class getAltitude extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            altitude = Utils.getElevationFromGoogleMaps(latitude, longitude);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(MainActivity.this, Camera.class);
            tempest = new ArrayList<>();
            tempest = getData();
            progressDialog.dismiss();
            startActivity(intent);
            progressDialog.dismiss();
        }
    };
}
