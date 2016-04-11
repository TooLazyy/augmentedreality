package com.example.artem.camera;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Artem on 15.02.2016.
 */
public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<HashMap<String,Object>> images;
    private LayoutInflater inflater;

    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<HashMap<String,Object>> array) {
        this.activity = activity;
        this.images = array;
    }

    @Override
    public int getCount() {
        return this.images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
        Button btnClose;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.item_full_screen_image, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        btnClose = (Button) viewLayout.findViewById(R.id.btnClose);

        String key="";
        for(String k:images.get(position).keySet()) {
            key = k;
        }
        Picasso.with(activity.getApplicationContext())
                .load(images.get(position).get(key).toString())
                .error(R.drawable.holder)
                .into(imgDisplay);
        //imgDisplay.setImageBitmap(images.get(position));
        TextView year = (TextView)viewLayout.findViewById(R.id.tvYear);
        year.setText(key+" год");
        // close button click event
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
