package com.example.artem.camera;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Artem on 06.03.2016.
 */
public class LikedObjectsActivity extends ActionBarActivity {

    private QBUser user;
    private ArrayList<HashMap<String,Object>> data;
    private QBRequestGetBuilder builder;
    private ListView list;
    private Dialog progresDialog;
    private ArrayAdapter<String> adapter;
    private List<String> titles;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liked_list);
        progresDialog = Utils.startLoadingDialog(this,"Идет загрузка!");
        list = (ListView)findViewById(R.id.lvLiked);
        data = new ArrayList<>();
        user = LoginActivity.getUser();
        builder = new QBRequestGetBuilder();
        builder.eq("login_user",user.getLogin().toString());
        if(user!=null) {
            getUserObjects();
            progresDialog.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_auth).setEnabled(false);
        menu.findItem(R.id.action_places).setEnabled(false);
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

        return super.onOptionsItemSelected(item);
    }

    private  void getUserObjects() {
        QBCustomObjects.getObjects(Consts.CLASS_LIKED_NAME, builder, new QBEntityCallback<ArrayList<QBCustomObject>>() {
            @Override
            public void onSuccess(ArrayList<QBCustomObject> qbCustomObjects, Bundle bundle) {
                if(DataHolderUserObjects.getDataHolder().size()>0) {
                    DataHolderUserObjects.getDataHolder().clear();
                }

                if(qbCustomObjects !=null && qbCustomObjects.size()>0) {
                    for(QBCustomObject object:qbCustomObjects) {
                        DataHolderUserObjects.getDataHolder().addObjectToList(object);
                    }
                    data = getData();
                    setUpList();
                    progresDialog.dismiss();
                } else {
                    progresDialog.dismiss();
                    Toast.makeText(LikedObjectsActivity.this,"Объектов не найдено",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(List<String> list) {
                progresDialog.dismiss();
                Toast.makeText(LikedObjectsActivity.this,"Ошибка!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<HashMap<String,Object>> getData() {
        ArrayList<HashMap<String,Object>> returnData = new ArrayList<>();
        if(DataHolderUserObjects.getDataHolder().size()>0){
            for(int i=0;i<DataHolderUserObjects.getDataHolder().size();i++){
                HashMap<String,Object> m = new HashMap<String, Object>();
                m.put(Consts.ATTRIBUTE_NAME,DataHolderUserObjects.getDataHolder().getName(i));
                m.put(Consts.ATTRIBUTE_DESCRIPTION,DataHolderUserObjects.getDataHolder().getDescription(i));
                m.put(Consts.ATTRIBUTE_LATITUDE,DataHolderUserObjects.getDataHolder().getLatitude(i));
                m.put(Consts.ATTRIBUTE_LONGITUDE, DataHolderUserObjects.getDataHolder().getLongitude(i));
                m.put(Consts.ATTRIBUTE_ID, DataHolderUserObjects.getDataHolder().getID(i));
                String urlArray[] = DataHolderUserObjects.getDataHolder()
                        .getUrls(i)
                        .replace("[","")
                        .replace("]","")
                        .replace("\"","")
                        .replace("\\", "")
                        .replace(" ","")
                        .split(",");
                String yearArray[] = DataHolderUserObjects.getDataHolder()
                        .getYears(i)
                        .replace("[", "")
                        .replace("]","")
                        .replace(" ","")
                        .split(",");
                Map yearUrl = new HashMap();
                for(int j=urlArray.length-1;j>=0;j--) {
                    yearUrl.put(yearArray[j], urlArray[j]);

                }
                if(yearUrl.size()>0) {
                    m.put(Consts.ATTRIBUTE_URLS, yearUrl);
                    returnData.add(m);
                }
            }

        }
        return returnData;
    }

    private  void setUpList() {
        titles = new ArrayList<String>();
        for (int i=0;i<data.size();i++) {
            titles.add(data.get(i).get(Consts.ATTRIBUTE_NAME).toString());
        }
        adapter = new ArrayAdapter<String>(
                LikedObjectsActivity.this,
                android.R.layout.simple_list_item_1,
                titles);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayItem(position);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                progresDialog.show();
                HashMap m = data.get(position);
                deleteLiked(m.get(Consts.ATTRIBUTE_ID).toString(),position);
                return  true;
            }
        });
    }

    private void displayItem(int i) {
        Intent intent = new Intent(LikedObjectsActivity.this,DescriptionActivity.class);
        intent.putExtra("name",data.get(i).get(Consts.ATTRIBUTE_NAME).toString());
        intent.putExtra("desc", data.get(i).get(Consts.ATTRIBUTE_DESCRIPTION).toString());
        HashMap<String,Object> urls = (HashMap)data.get(i).get(Consts.ATTRIBUTE_URLS);
        intent.putExtra("urls", urls);
        startActivity(intent);
    }

    private void deleteLiked(String id, final int position) {
        QBCustomObject object = new QBCustomObject("liked",id);
        QBCustomObjects.deleteObject(object, new QBEntityCallback() {
            @Override
            public void onSuccess(Object o, Bundle bundle) {
            }

            @Override
            public void onSuccess() {
                Toast.makeText(LikedObjectsActivity.this,"Готово!",Toast.LENGTH_SHORT).show();
                data.remove(position);
                titles.remove(position);
                adapter.notifyDataSetChanged();
                progresDialog.dismiss();
            }

            @Override
            public void onError(List list) {
                progresDialog.dismiss();
                Toast.makeText(LikedObjectsActivity.this,"Ошибка, попробуйте ещё раз.",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
