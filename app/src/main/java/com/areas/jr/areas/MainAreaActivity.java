package com.areas.jr.areas;

import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import com.parse.Parse;
import com.parse.ParseObject;

public class MainAreaActivity extends ActionBarActivity {

    AreaAdapter areaListAdapter;
    ListView areaDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_area);

        areaListAdapter = new AreaAdapter(this);

        areaDesc = (ListView)findViewById(R.id.listView1);
        areaDesc.setAdapter(areaListAdapter);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "QhKfyUGLckCWZVun2rcciapdhZvdFW3eAi9v3INe", "AlPshZhdLdI8ao8tPU949MB1F4DTDTU5J1geXi9H");

        //Test Parse
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_area, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<AreaInfo> getDataForListView()
    {
        List<AreaInfo> areaList = new ArrayList<AreaInfo>();

        for(int i=0;i<10;i++)
        {

            AreaInfo area = new AreaInfo();
            area.id = i;
            area.name = "Repsol";
            area.gas_station = 1;
            area.garage = 1;
            area.restaurant = 1;
            area.situation = 0;
            //area.position.latitude = 20.0;

            areaList.add(area);
        }

        return areaList;
    }
}
