package com.example.artem.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Artem on 27.08.2015.
 */
public class DescriptionActivity extends ActionBarActivity {

    private String o_name;
    private String distance;
    private String o_desc;
    private HashMap<String,Object> urls;
    private TextView tvDescr;
    private TextView tvDist;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        o_name = getIntent().getStringExtra("name");
        o_desc = getIntent().getStringExtra("desc");
        urls = (HashMap)getIntent().getSerializableExtra("urls");
        try {
            distance = getIntent().getStringExtra("dist");
            int index = distance.indexOf(".");
            distance = distance.substring(0, index + 5);
        } catch (Exception ex) {
            distance = "?";
        }
        name = (TextView)findViewById(R.id.oName);
        name.setText(o_name);

        ImageView image = (ImageView)findViewById(R.id.ivImage);

        Map.Entry<String,Object> firstEntry = urls.entrySet().iterator().next();
        Picasso.with(this)
                .load(firstEntry.getValue().toString())
                .error(R.drawable.ic_launcher)
                .into(image);
        tvDescr = (TextView)findViewById(R.id.oDescr);
        tvDescr.setText(o_desc);

        tvDist = (TextView)findViewById(R.id.oDist);
        String formatted="";
        if(!distance.equals("?")) {
            formatted = new DecimalFormat("#0.00").format(Double.valueOf(distance) * 1000);
            formatted = formatted.split(",")[0];
        } else {
            formatted = distance;
        }
        tvDist.setText("Расстояние до объекта: "+formatted+" метров.");

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DescriptionActivity.this, FullScreenViewActivity.class);
                intent.putExtra("urls", urls);
                startActivity(intent);
            }
        });


    }
}
