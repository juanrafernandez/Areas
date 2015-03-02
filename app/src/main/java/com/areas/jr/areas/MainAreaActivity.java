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

 /*   public class AreaAdapter  extends BaseAdapter {

        List<AreaInfo> areasList = getDataForListView();

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return areasList.size();
        }

        @Override
        public AreaInfo getItem(int arg0) {
            // TODO Auto-generated method stub
            return areasList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            if(arg1==null)
            {
                LayoutInflater inflater = (LayoutInflater) MainAreaActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.area_row, arg2,false);
            }

            TextView chapterName = (TextView)arg1.findViewById(R.id.textView1);
            TextView chapterDesc = (TextView)arg1.findViewById(R.id.textView2);

            AreaInfo area = areasList.get(arg0);

            chapterName.setText(area.name);
            chapterDesc.setText(area.id);

            return arg1;
        }

    }*/

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
