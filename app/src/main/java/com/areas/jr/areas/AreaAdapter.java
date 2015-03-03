package com.areas.jr.areas;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 108001 on 02/03/2015.
 */
public class AreaAdapter  extends BaseAdapter {

    List<AreaInfo> areasList = getDataForListView();
    private final Activity context;

    public AreaAdapter(Activity context) {
       // super(context, R.layout.activity_main_area, itemname);
        // TODO Auto-generated constructor stub
        this.context=context;
    }

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
            LayoutInflater inflater=context.getLayoutInflater();
            arg1=inflater.inflate(R.layout.area_row, null,true);
        }

        TextView chapterName = (TextView)arg1.findViewById(R.id.textView1);
        TextView chapterDesc = (TextView)arg1.findViewById(R.id.textView2);

        AreaInfo area = areasList.get(arg0);

        chapterName.setText(area.name);
        chapterDesc.setText(area.name);

        return arg1;
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