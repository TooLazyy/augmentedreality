package com.example.artem.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Artem on 08.07.2015.
 */
public class Camera extends FragmentActivity implements
        OnClickBeyondarObjectListener {

    public static ArrayList <Map<String,Object>> myUsedObjects;
    private BeyondarFragmentSupport mBeyondarFragment;
    private World mWorld;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        myUsedObjects = new ArrayList<Map<String,Object>>();

        setContentView(R.layout.camera);

        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.beyondarFragment);
        // We create the world and fill it ...
        mWorld = CustomWorldHelper.generateObjects(this,MainActivity.tempest);
        // ... and send it to the fragment
        mBeyondarFragment.setMaxDistanceToRender(700);
        //Set the distance factor for rendering all the objects.
        //As bigger the factor the closer the objects.
        mBeyondarFragment.setDistanceFactor(6);


        mBeyondarFragment.setPushAwayDistance(7);
        mBeyondarFragment.setWorld(mWorld);
        // We also can see the Frames per seconds
        mBeyondarFragment.showFPS(true);

        //mBeyondarFragment.setOnTouchBeyondarViewListener(this);
        mBeyondarFragment.setOnClickBeyondarObjectListener(this);
        if(CustomWorldHelper.usedObjects.size()!=0) {
            myUsedObjects = CustomWorldHelper.usedObjects;
        }
    }

    @Override
    public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
        if (beyondarObjects.size() > 0) {
            Intent intent = new Intent(this,DescriptionActivity.class);
            for(int i = 0;i<myUsedObjects.size();i++){
                if(myUsedObjects.get(i).get("name").toString()==beyondarObjects.get(0).getName()){
                    intent.putExtra("name",myUsedObjects.get(i).get("name").toString());
                    intent.putExtra("desc",myUsedObjects.get(i).get("desc").toString());
                    intent.putExtra("dist", myUsedObjects.get(i).get("dist").toString());
                    HashMap<String,Object> urls = (HashMap)myUsedObjects.get(i).get("urls");
                    intent.putExtra("urls", urls);
                    startActivity(intent);
                }
            }
        }
    }
}

