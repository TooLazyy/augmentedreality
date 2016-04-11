package com.example.artem.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.validation.Validator;

/**
 * Created by Artem on 15.02.2016.
 */
public class FullScreenViewActivity extends Activity {

    private FullScreenImageAdapter mAdapter;
    private ViewPager viewPager;
    private ArrayList<HashMap<String,Object>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        viewPager = (ViewPager)findViewById(R.id.pager);

        HashMap<String,Object> photosArray = (HashMap)getIntent().getSerializableExtra("urls");
        data = new ArrayList<>();
        data = fillDataArray(photosArray);
        mAdapter = new FullScreenImageAdapter(FullScreenViewActivity.this,data);
        viewPager.setAdapter(mAdapter);
    }

    private ArrayList<HashMap<String,Object>> fillDataArray(HashMap m) {
        Iterator iterator = m.entrySet().iterator();
        ArrayList<HashMap<String,Object>> returnData = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            HashMap<String,Object> temp = new HashMap<>();
            temp.put(pair.getKey().toString(),pair.getValue());
            returnData.add(temp);
            iterator.remove();
        }
        return returnData;
    }
}
