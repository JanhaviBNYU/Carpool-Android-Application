/// Code Referred from http://developer.android.com/guide/topics/ui/layout/gridview.html
package com.example.janhavibagwe.carpoolapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SearchObject> gridValues;


    public GridAdapter(Context context, ArrayList<SearchObject> searchList) {

        this.context        = context;
        this.gridValues     = searchList;
    }

    @Override
    public int getCount() {

        // Number of times getView method call depends upon gridValues.length
        return gridValues.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }


    // Number of times getView method call depends upon gridValues.length

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {
            gridView = inflater.inflate( R.layout.grid , null);


            ((TextView) gridView.findViewById(R.id.GStart)).setText(gridValues.get(position).start);
            ((TextView) gridView.findViewById(R.id.GDest)).setText(gridValues.get(position).destination);
            ((TextView) gridView.findViewById(R.id.GDate)).setText(gridValues.get(position).date);
            ((TextView) gridView.findViewById(R.id.GTime)).setText(gridValues.get(position).time);
            ((TextView) gridView.findViewById(R.id.GFare)).setText("$" + String.valueOf(gridValues.get(position).fare));
            ((TextView) gridView.findViewById(R.id.GContact)).setText("Contact:" + gridValues.get(position).contact);
            ((TextView) gridView.findViewById(R.id.GPhone)).setText("Call:" + gridValues.get(position).phone);

        } else {

            gridView = (View) convertView;
        }

        return gridView;
    }
}
